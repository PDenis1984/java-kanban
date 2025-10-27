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
        );
        this.inMemoryHistoryManager = Managers.getDefaultHistoryManager();
    }

    // Набор для получения задач
    @Override
    public Task getTaskByID(int mID) {

        if (taskList.containsKey(mID)) {
            Task task = taskList.get(mID);
            inMemoryHistoryManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic getEpicByID(int mID) {

        if (!epicList.isEmpty()) {
            Epic epic = epicList.get(mID);
            inMemoryHistoryManager.add(epic);
            return epic;
        }
        return null;
    }

    private Epic getEpicByIDForSubTask(int mID) {

        if (!epicList.isEmpty()) {
            Epic epic = epicList.get(mID);
            return epic;
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
    public SubTask getSubTaskByID(int mSubTaskID) {

        if (subTaskList.containsKey(mSubTaskID)) {
            SubTask subTask = subTaskList.get(mSubTaskID);
            inMemoryHistoryManager.add(subTask);
            return subTask;
        }
        return null;
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
        Epic epic = epicList.get(Integer.valueOf(mEpicID));
        if (epic != null) {
            ArrayList<Integer> subTaskNumbers = epic.getAllSubTask();
            for (int number : subTaskNumbers) {
                subTaskArrayList.add(subTaskList.get(Integer.valueOf(number)));
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
    public int createTask(Task mTask) {

        int taskID = getElementID();
        mTask.setID(taskID);
        taskList.put(taskID, mTask);
        try {
            if (mTask.getStartTime() != null) {

                if (!checkTaskOverlap(mTask)) {

                    addPrioritizeTask(mTask);
                } else {
                    mTask.setStartTime(null);
                    mTask.setDuration(null);
                    throw new TaskOverlapException("Задача пересекается по времени с уже существующей");//Подумать над выносом в интерфейс
                }
            }
        } catch (TaskOverlapException toEx) {

            System.out.println(toEx.getMessage());
        }
        return taskID;
    }

    @Override
    public int createSubTask(SubTask mSubTask) {

        int subTaskID = getElementID();
        Epic epic = getEpicByIDForSubTask(mSubTask.getEpicID());
        if (epic != null) {
            mSubTask.setID(subTaskID);
            subTaskList.put(subTaskID, mSubTask);
            epic.addSubTask(subTaskID);
            try {
                if (mSubTask.getStartTime() != null) {

                    if (!checkTaskOverlap(mSubTask)) {

                        addPrioritizeTask(mSubTask);
                        recountEpicTimes(epic);
                    } else {
                        mSubTask.setDuration(null);
                        mSubTask.setStartTime(null);
                        throw new TaskOverlapException("Подзадача пересекается по времени с уже существующей");//Подумать над выносом в интерфейс
                    }
                }
            } catch (TaskOverlapException toEx) {

                System.out.println(toEx.getMessage());
            }
            recountEpicState(epic);
            return subTaskID;
        } else {
            System.out.println("Указан неверный номер Эпика");
            return -1;
        }
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
                        subTaskList.remove(subTaskNumber);
                        inMemoryHistoryManager.remove(subTaskNumber);
                    }
                    epicList.remove(mID);
                    inMemoryHistoryManager.remove(mID);
                    prioritizeTasks.remove(epic);
                } else {
                    System.out.println("Передан неверный номер Эпика");
                }
                break;
            case "TASK":

                if (taskList.containsKey(mID)) {
                    taskList.remove(mID);
                    inMemoryHistoryManager.remove(mID);
                    prioritizeTasks.remove(taskList.get(mID));
                } else {
                    System.out.println("Передан неверный номер Задачи: " + mID);
                }
                break;
            case "SUBTASK":

                if (subTaskList.containsKey(mID)) {
                    subTaskList.remove(mID);
                    Epic epic = getEpicBySubTaskID(mID);
                    if (epic != null) {
                        epic.deleteSubTaskByID(mID);
                        recountEpicState(epic);
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
                }
                subTaskList.clear(); // Все сабтаски принадлежат эпикам - потому удаляем их все

                for (Integer epicNumber : epicList.keySet()) {

                    inMemoryHistoryManager.remove(epicNumber);
                }
                epicList.clear();

                break;
            case "TASK":

                for (Integer taskNumber : taskList.keySet()) {

                    inMemoryHistoryManager.remove(taskNumber);
                }
                taskList.clear();
                break;
            case "SUB_TASK":

                for (Integer subTaskNumber : subTaskList.keySet()) {

                    inMemoryHistoryManager.remove(subTaskNumber);
                }
                subTaskList.clear();  //Очищаем все сабтакси и обновляем статус у Эпика
                for (Epic epic : epicList.values()) {

                    epic.deleteAllSubTask();
                    recountEpicState(epic);
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
    public void updateEpic(Epic mEpic) {

        Epic etalonEpic = epicList.get(mEpic.getID());
        if (epicList.containsKey(mEpic.getID())) {
            if ((etalonEpic.getState() == mEpic.getState()) && ((etalonEpic.getAllSubTask()).equals(mEpic.getAllSubTask()))) {
                epicList.put(mEpic.getID(), mEpic);
            } else {
                System.out.println("Возможно обновление полей 'Name' и/или 'Description'");
            }

        }
    }

    @Override
    public void updateTask(Task mTask) {

        if (taskList.containsKey(mTask.getID())) {
            taskList.put(mTask.getID(), mTask);
            if (mTask.getStartTime() != null) {

                try {
                    if (!checkTaskOverlap(mTask)) {
                        addPrioritizeTask(mTask);
                    } else {
                        mTask.setDuration(null);
                        mTask.setStartTime(null);
                        throw new TaskOverlapException("Задача пересекается по времени с уже существующей");
                    }
                } catch (TaskOverlapException toEx) {
                    System.out.println(toEx.getMessage());
                }
            }
        } else {
            System.out.println("Передан неверный номер задачи");
        }
    }

    @Override
    public void updateSubTask(SubTask mSubTask) {

        SubTask etalonSubTask = getSubTaskByID(mSubTask.getID());
        if (subTaskList.containsKey(mSubTask.getID())) {
            if (mSubTask.getEpicID() == etalonSubTask.getEpicID()) {
                subTaskList.put(mSubTask.getID(), mSubTask);
                Epic epic = epicList.get(mSubTask.getEpicID());
                recountEpicState(epic);
                try {
                    if (mSubTask.getStartTime() != null) {
                        if (!checkTaskOverlap(mSubTask)) {
                            addPrioritizeTask(mSubTask);
                            recountEpicTimes(epic);
                        } else {
                            mSubTask.setDuration(null);
                            mSubTask.setStartTime(null);
                            throw new TaskOverlapException("Подзадача пересекается по времени с уже существующей");
                        }
                    }
                } catch (TaskOverlapException toEx) {
                    System.out.println(toEx.getMessage());
                }

            } else {
                System.out.println("Запрещено обновлять поля, кроме 'Name' и/или 'Description'");
            }
        }
    }

    @Override
    public void printAllElements(String mType) {

        System.out.println("=====".repeat(5));
        switch (mType) {
            case "TASK":
                for (Task task : taskList.values()) {
                    System.out.println(task);
                }
                break;
            case "EPIC":
                for (Epic epic : epicList.values()) {
                    System.out.println(epic);
                }
                break;
            case "SUB_TASK":
                for (SubTask subTask : subTaskList.values()) {
                    System.out.println(subTask);
                }
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

        LocalDateTime startTime = mEpic.getStartTime();
        LocalDateTime endTime = mEpic.getEndTime();
        LocalDateTime newEndTime = mEpic.getAllSubTask().stream()
                .map(this::getSubTaskByID)
                .filter(Objects::nonNull)
                .map(SubTask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(endTime);
        mEpic.setEndTime(newEndTime);

        LocalDateTime newStartTime = mEpic.getAllSubTask().stream()
                .map(this::getSubTaskByID)
                .filter(Objects::nonNull)
                .map(SubTask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(startTime);
        mEpic.setStartTime(newStartTime);

        Duration duration = mEpic.getAllSubTask().stream()
                .map(this::getSubTaskByID)
                .filter(Objects::nonNull)
                .map(SubTask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
        mEpic.setDuration(duration);
    }

    public boolean checkTaskOverlap(Task mTask) {

        // Если время начала задачи - меньше startTime и время окончания меньше startTime, а также если время начала задачи больше endTime
        //то это непересекающаяся задача. если есть хоты бы одно исключение - то задача пересекается

        return !prioritizeTasks.stream()
                .allMatch(task -> (mTask.getStartTime().isBefore(task.getStartTime()) && mTask.getEndTime().isBefore(mTask.getStartTime()))
                        || (mTask.getStartTime().isAfter(task.getStartTime())));
    }

    private void addPrioritizeTask(Task mTAsk) {

        if (prioritizeTasks.contains(mTAsk)) {
            prioritizeTasks.remove(mTAsk);
        }
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
