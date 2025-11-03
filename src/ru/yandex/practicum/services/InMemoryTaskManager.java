package ru.yandex.practicum.services;

import ru.yandex.practicum.intf.HistoryManagerIntf;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskState;
import ru.yandex.practicum.models.exceptioons.TaskOverlapException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManagerIntf {

    // Поменял на protected, чтобы использовать в файловом менеджере
    protected int elementID; // Сквозная нумерация задач
    protected Map<Integer, Task> taskList;
    protected Map<Integer, Epic> epicList;
    protected Map<Integer, SubTask> subTaskList;
    protected HistoryManagerIntf inMemoryHistoryManager;
    protected Set<Task> prioritizeTasks;

    public InMemoryTaskManager() {
        taskList = new HashMap<Integer, Task>();
        epicList = new HashMap<Integer, Epic>();
        subTaskList = new HashMap<Integer, SubTask>();
        elementID = 0;
        prioritizeTasks = new TreeSet<>(
                Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Task::getID)
        );
        this.inMemoryHistoryManager = Managers.getDefaultHistoryManager();
    }

    // Набор для получения задач
    @Override
    public Optional<Task> getTaskByID(int mID) {

        if (taskList.containsKey(mID)) {
            Task task = taskList.get(mID);
            inMemoryHistoryManager.add(task);
            return Optional.of(task);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Epic> getEpicByID(int mID) {

        if (!epicList.isEmpty()) {
            Optional<Epic> epic = Optional.ofNullable(epicList.get(mID));
            epic.ifPresent(value -> inMemoryHistoryManager.add(value));
            return epic;
        }
        return Optional.empty();
    }

    private Epic getEpicByIDForSubTask(int mID) {

        if (!epicList.isEmpty()) {

            return epicList.get(mID);
        }
        return null;
    }


    @Override
    public Epic getEpicBySubTaskID(int mSubtaskID) {

        for (Epic epic : epicList.values()) {
            if (epic.getAllSubTask().contains(mSubtaskID)) {
                inMemoryHistoryManager.add(epic);
                return epic;
            }
        }
        return null;
    }

    @Override
    public Optional<SubTask> getSubTaskByID(int mSubTaskID) {

        if (subTaskList.containsKey(mSubTaskID)) {
            SubTask subTask = subTaskList.get(mSubTaskID);
            inMemoryHistoryManager.add(subTask);
            return Optional.of(subTask);
        }
        return Optional.empty();
    }


    @Override
    public ArrayList<Epic> getAllEpic() {

        ArrayList<Epic> epicArrayList = new ArrayList<Epic>();
        for (Epic epic : epicList.values()) {
            epicArrayList.add(epic);
        }
        return epicArrayList;
    }

    @Override
    public ArrayList<Task> getAllTasks() {

        ArrayList<Task> taskArrayList = new ArrayList<Task>();
        for (Task task : taskList.values()) {
            taskArrayList.add(task);
        }
        return taskArrayList;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {

        ArrayList<SubTask> subTaskArrayList = new ArrayList<SubTask>();
        for (SubTask subTask : subTaskList.values()) {
            subTaskArrayList.add(subTask);
        }
        return subTaskArrayList;
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskByEpicID(int mEpicID) {

        ArrayList<SubTask> subTaskArrayList = new ArrayList<SubTask>();
        Epic epic = epicList.get(mEpicID);
        if (epic != null) {
            ArrayList<Integer> subTaskNumbers = epic.getAllSubTask();
            for (int number : subTaskNumbers) {
                subTaskArrayList.add(subTaskList.get(number));
            }
            return subTaskArrayList;
        } else {
            return null;
        }
    }

    //Создание задач всех типов
    @Override
    public int createEpic(Epic mEpic) {

        int epicID = getElementID();
        mEpic.setID(epicID);
        epicList.put(epicID, mEpic);
        return epicID;
    }

    @Override
    public Optional<Integer> createTask(Task mTask) throws TaskOverlapException {

        if (!hasTimeConflict(mTask)) {
            int taskID = getElementID();
            mTask.setID(taskID);
            taskList.put(taskID, mTask);
            if (mTask.getStartTime() != null) {

                addPrioritizeTask(mTask);
            }
            return Optional.of(taskID);
        } else {
            throw new TaskOverlapException("Задача пересекается по времени с уже существующими задачами или подзадачами");
        }
    }

    @Override
    public Optional<Integer> createSubTask(SubTask mSubTask) {


        Epic epic = getEpicByIDForSubTask(mSubTask.getEpicID());
        if (epic != null) {

            if (!hasTimeConflict(mSubTask)) {

                int subTaskID = getElementID();
                mSubTask.setID(subTaskID);
                subTaskList.put(subTaskID,mSubTask);
                epic.addSubTask(subTaskID);
                if (mSubTask.getStartTime() != null) {

                    addPrioritizeTask(mSubTask);
                    recountEpicTimes(epic);
                }
                recountEpicState(epic);
                return Optional.of(subTaskID);
            }
        } else {
            System.out.println("Указан неверный номер Эпика");
        }
        return Optional.empty();
    }

    // Удаление
    @Override
    public void deleteElement(int mID, String mType) {

        switch (mType) {
            case "EPIC":

                if (epicList.containsKey(mID)) {
                    Epic epic = epicList.get(mID);
                    ArrayList<Integer> subTaskNumbers = epic.getAllSubTask();
                    for (Integer subTaskNumber : subTaskNumbers) {

                        prioritizeTasks.remove(subTaskList.get(subTaskNumber));
                        subTaskList.remove(subTaskNumber);
                        inMemoryHistoryManager.remove(subTaskNumber);
                    }
                    epicList.remove(mID);
                    inMemoryHistoryManager.remove(mID);
                } else {
                    System.out.println("Передан неверный номер Эпика");
                }
                break;
            case "TASK":

                if (taskList.containsKey(mID)) {

                    prioritizeTasks.remove(taskList.get(mID));
                    taskList.remove(mID);
                    inMemoryHistoryManager.remove(mID);
                } else {
                    System.out.println("Передан неверный номер Задачи: " + mID);
                }
                break;
            case "SUBTASK":

                if (subTaskList.containsKey(mID)) {

                    prioritizeTasks.remove(subTaskList.get(mID));
                    subTaskList.remove(mID);
                    Epic epic = getEpicBySubTaskID(mID);
                    if (epic != null) {

                        epic.deleteSubTaskByID(mID);
                        recountEpicState(epic);
                        recountEpicTimes(epic);
                    }
                    inMemoryHistoryManager.remove(mID);
                } else {
                    System.out.println("Передан неверный номер Подзадачи");
                }
                break;
            default:

                System.out.println("Передан неверный тип задачи");
        }
    }

    @Override
    public void deleteAllElements(String mType) {

        switch (mType) {
            case "EPIC":

                for (Integer subTaskNumber : subTaskList.keySet()) {

                    inMemoryHistoryManager.remove(subTaskNumber);
                    prioritizeTasks.remove(subTaskList.get(subTaskNumber));
                }

                subTaskList.clear(); // Все сабтаски принадлежат эпикам - потому удаляем их все

                for (Integer epicNumber : epicList.keySet()) {

                    inMemoryHistoryManager.remove(epicNumber);
                }
                epicList.clear();

                break;
            case "TASK":

                for (Integer taskNumber : taskList.keySet()) {

                    prioritizeTasks.remove(taskList.get(taskNumber));
                    inMemoryHistoryManager.remove(taskNumber);
                }
                taskList.clear();
                break;
            case "SUB_TASK":

                for (Integer subTaskNumber : subTaskList.keySet()) {

                    prioritizeTasks.remove(subTaskList.get(subTaskNumber));
                    inMemoryHistoryManager.remove(subTaskNumber);
                }
                subTaskList.clear();  //Очищаем все сабтакси и обновляем статус у Эпика
                for (Epic epic : epicList.values()) {

                    epic.deleteAllSubTask();
                    recountEpicState(epic);
                    recountEpicTimes(epic);
                }
                break;
            default:

                System.out.println("Передан неверный тип задачи");
        }
    }

    private int getElementID() {
        elementID++;
        return elementID;
    }

    //Обновление
    @Override
    public boolean updateEpic(Epic mEpic) {

        Epic etalonEpic = epicList.get(mEpic.getID());
        if (epicList.containsKey(mEpic.getID())) {
            if ((etalonEpic.getState() == mEpic.getState()) && ((etalonEpic.getAllSubTask()).equals(mEpic.getAllSubTask()))) {
                epicList.put(mEpic.getID(), mEpic);
                return true;
            } else {

                System.out.println("Возможно обновление полей 'Name' и/или 'Description'");
                return false;
            }
        } else  {
            System.out.println("Не найден обновляемый эпик");
            return false;
        }
    }

    @Override
    public boolean updateTask(Task mTask) {

        if (taskList.containsKey(mTask.getID())) {

            if (!hasTimeConflict(mTask)) {

                taskList.put(mTask.getID(), mTask);
                prioritizeTasks.remove(mTask);
                if (mTask.getStartTime() != null) {

                    addPrioritizeTask(mTask);
                }
                return true;
            } else {
                System.out.println("Задача не должна пересекаться по времени с существующими");
                return false;
            }
        } else {
            System.out.println("Передан неверный номер задачи");
            return false;
        }
    }

    @Override
    public boolean updateSubTask(SubTask mSubTask) {

        SubTask etalonSubTask = subTaskList.get(mSubTask.getID());
        if (subTaskList.containsKey(mSubTask.getID())) {
            if (mSubTask.getEpicID() == etalonSubTask.getEpicID()) {

                prioritizeTasks.remove(mSubTask);
                if (!hasTimeConflict(mSubTask)) {

                    subTaskList.put(mSubTask.getID(), mSubTask);
                    Epic epic = epicList.get(mSubTask.getEpicID());
                    recountEpicState(epic);
                    if (mSubTask.getStartTime() != null) {

                        addPrioritizeTask(mSubTask);
                        recountEpicTimes(epic);
                    }
                    return true;
                } else {
                    throw new TaskOverlapException("Подзадача не должна пересекаться по времени с существующими");
                }

            } else {
                System.out.println("Запрещено обновлять поля, кроме полей 'Name' 'Description', 'StartTime, 'Duration'");
            }
        } else {
            System.out.println("Подзадача не найдена");
        }
        return false;
    }

    @Override
    public boolean isTaskExists(int mTaskId) {

        return taskList.containsKey(mTaskId);
    }

    @Override
    public boolean isEpicExists(int mTaskId) {

        return epicList.containsKey(mTaskId);
    }

    @Override
    public boolean isSubTaskExists(int mSubTaskId) {

        return subTaskList.containsKey(mSubTaskId);
    }

    @Override
    public void printAllElements(String mType) {

        System.out.println("=====".repeat(5));
        switch (mType) {
            case "TASK":
                taskList.values().forEach(System.out::println);
                break;
            case "EPIC":
                epicList.values().forEach(System.out::println);
                break;
            case "SUB_TASK":
                subTaskList.values().forEach(System.out::println);
                break;
        }
        System.out.println("=====".repeat(5));
    }

    protected void recountEpicState(Epic mEpic) {

        int allState;
        int nState = 0;
        int inState = 0;
        int dState = 0;
        ArrayList<Integer> subTaskElements = mEpic.getAllSubTask();
        allState = subTaskElements.size();
        for (int subTask : subTaskElements) {
            if (subTaskList.get(subTask).getState() == TaskState.NEW) {
                nState++;
            } else if (subTaskList.get(subTask).getState() == TaskState.IN_PROGRESS) {
                inState++;
            } else {
                dState++;
            }
        }
        if (nState == allState) {
            mEpic.setState(TaskState.NEW);
        } else if (dState == allState) {
            mEpic.setState(TaskState.DONE);
        } else {
            mEpic.setState(TaskState.IN_PROGRESS);
        }
    }

    protected void recountEpicTimes(Epic mEpic) { //

        LocalDateTime newEndTime = mEpic.getAllSubTask().stream()
                .map(subTaskNumber -> subTaskList.get(subTaskNumber))
                .filter(Objects::nonNull)
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        mEpic.setEndTime(newEndTime);

        LocalDateTime newStartTime = mEpic.getAllSubTask().stream()
                .map(subTaskNumber -> subTaskList.get(subTaskNumber))
                .filter(Objects::nonNull)
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        mEpic.setStartTime(newStartTime);

        Duration duration = mEpic.getAllSubTask().stream()
                .map(subTaskNumber -> subTaskList.get(subTaskNumber))
                .filter(Objects::nonNull)
                .map(SubTask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
        mEpic.setDuration(duration);
    }

    public boolean hasTimeConflict(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null) {
            return false; // Задачи без времени не конфликтуют
        }

        return prioritizeTasks.stream()
                .filter(existingTask -> !existingTask.equals(newTask)) // Исключаем саму задачу
                .filter(existingTask -> existingTask.getStartTime() != null &&
                        existingTask.getEndTime() != null) // Исключаем задачи без времени
                .anyMatch(existingTask -> isTimeOverlap(newTask, existingTask));
    }

    private boolean isTimeOverlap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        // Два интервала пересекаются, если:
        // начало первого < конца второго И начало второго < конца первого
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private void addPrioritizeTask(Task mTAsk) {

        prioritizeTasks.add(mTAsk);
    }

    @Override
    public List<Task> getHistory() {

        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizeTasks() {


        return new ArrayList<Task>(this.prioritizeTasks.stream()
                .peek(x -> this.inMemoryHistoryManager.add(x))
                .toList());
    }
}
