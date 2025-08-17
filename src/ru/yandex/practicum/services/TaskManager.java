package ru.yandex.practicum.services;

import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskState;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager implements TaskManagerIntf {

    private int elementID; // Сквозная нумерация задач
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private HashMap<Integer, SubTask> subTaskList;

    public TaskManager() {
        taskList = new HashMap<Integer, Task>();
        epicList = new HashMap<Integer, Epic>();
        subTaskList = new HashMap<Integer, SubTask>();
        elementID = 0;

    }

    // Набор для получения задач
    @Override
    public Task getTaskByID(int mID) {

        if (!taskList.isEmpty()) {
            return taskList.get(mID);
        }
        return null;
    }

    @Override
    public Epic getEpicByID(int mID) {

        if (!epicList.isEmpty()) {
            return epicList.get(mID);
        }
        return null;
    }


    @Override
    public Epic getEpicBySubTaskID(int mSubtaskID) {

        for (Epic epic : epicList.values()) {
            if (epic.getAllSubTask().contains(mSubtaskID)) {
                return epic;
            }
        }
        return null;
    }

    @Override
    public SubTask getSubTaskByID(int mSubTaskID) {

        if (subTaskList.containsKey(mSubTaskID)) {
            return subTaskList.get(mSubTaskID);
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
        return taskID;
    }

    @Override
    public int createSubtask(SubTask mSubTask) {

        int subTaskID = getElementID();
        Epic epic = getEpicByID(mSubTask.getEpicID());
        if (epic != null) {
            mSubTask.setID(subTaskID);
            subTaskList.put(subTaskID, mSubTask);
            epic.addSubTask(subTaskID);
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
                    }
                    epicList.remove(mID);
                } else {
                    System.out.println("Передан неверный номер Эпика");
                }
                break;
            case "TASK":

                if (taskList.containsKey(mID)) {
                    taskList.remove(mID);
                } else {
                    System.out.println("Передан неверный номер Задачи: " + mID);
                }
                break;
            case "SUB_TASK":

                if (subTaskList.containsKey(mID)) {
                    subTaskList.remove(mID);
                    Epic epic = getEpicBySubTaskID(mID);
                    if (epic != null) {
                        epic.deleteSubTaskByID(mID);
                        recountEpicState(epic);
                    }
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

                subTaskList.clear(); // Все сабтаски принадлежат эпикам - потому удаляем их все
                epicList.clear();
                break;
            case "TASK":

                taskList.clear();
                break;
            case "SUB_TASK":

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
            } else {
                System.out.println("Запрещено обновлять поля, кроме 'Name' и/или 'Description'");
            }
        }
    }

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
}
