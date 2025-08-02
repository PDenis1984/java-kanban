package ru.yandex.practicum.models;

import java.util.HashMap;

public class TaskManager {

    static int elementID; // Сквозная нумерация задач
    private static HashMap<Integer, Task> taskList;
    private static HashMap<Integer, Epic> epicList;
    private static HashMap<Integer, SubTask> subTaskList;

    public TaskManager() {
        taskList = new HashMap<Integer, Task>();
        epicList = new HashMap<Integer, Epic>();
        subTaskList = new HashMap<Integer, SubTask>();
        elementID = 0;

    }

    // Набор для получения задач
    static Task getTaskByID(int mID) {

        if (!taskList.isEmpty()) {
            return taskList.get(mID);
        }
        return null;
    }

    static Epic getEpicByID(int mID) {

        if (!epicList.isEmpty()) {
            return epicList.get(mID);
        }
        return null;
    }


    static Epic getEpicBySubtaskID(int mSubtaskID) {

        for (Epic epic : epicList.values()) {
            if (epic.subTaskElements.contains(mSubtaskID)) {
                return epic;
            }
        }
        return null;
    }

    static HashMap<Integer, Epic> getAllEpic() {

        return epicList;
    }

    static HashMap<Integer, Task> getAllTasks(){

        return taskList;
    }

    static HashMap<Integer, SubTask> getAllSubTasks() {

        return subTaskList;
    }

    //Создание задач всех типов
    public void createEpic(Epic mEpic) {

        epicList.put(mEpic.getID(), mEpic);
    }

    public  void createTask(Task task) {

        taskList.put(task.getID(), task);
    }

    public  void createSubtask(int mEpicID, SubTask subTask) {

        Epic epic = getEpicByID(mEpicID);
        if (epic != null) {
            subTaskList.put(subTask.getID(), subTask);
            epic.subTaskElements.add(subTask.getID());
            epic.recountEpicState(subTaskList);

        } else {
            System.out.println("Указан неверный номер Эпика");
        }
    }


    // Удаление
    public static void deleteElement(int mID, String mType) {

        switch (mType) {
            case "EPIC":

                if (epicList.containsKey(mID)) {
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
                    Epic epic = getEpicBySubtaskID(mID);
                    if (epic != null) {
                        epic.subTaskElements.remove(Integer.valueOf(mID));
                        epic.recountEpicState(subTaskList);
                    }
                } else {
                    System.out.println("Передан неверный номер Подзадачи");
                }
                break;
            default:
                System.out.println("Передан неверный тип задачи");
        }
    }

    public static void deleteAllElements(String mType) {

        switch (mType) {
            case "EPIC":

                epicList.clear();
                break;
            case "TASK":

                taskList.clear();
                break;
            case "SUB_TASK":

                subTaskList.clear();  //Очищаем все сабтакси и обновляем статус у Эпика
                for (Epic epic : epicList.values()) {
                    epic.subTaskElements.clear();
                    epic.setState(TaskState.NEW);
                }
                break;
            default:
                System.out.println("Передан неверный тип задачи");
        }
    }

    public static int getElementID() {
        elementID++;
        return elementID;
    }

    //Обновление
    public  void updateEpic(int epicID, Epic epic) {

        epicList.put(epicID, epic);
    }

    public  void updateTask(int taskID, Task task) {

        taskList.put(taskID, task);
        task.updateState();
    }

    public void updateSubTask(int mSubTaskID, SubTask mSubTask) {

        subTaskList.put(mSubTaskID, mSubTask);
        Epic epic = getEpicBySubtaskID(mSubTaskID);
        mSubTask.updateState();
        epic.recountEpicState(subTaskList);
    }

    public static void printAllElements(String mType) {
        
        System.out.println("=====".repeat(5));
        switch (mType) {
            case "TASK":
                for (Task task: taskList.values()){
                    System.out.println(task);
                }
                break;
            case "EPIC":
                for (Epic epic: epicList.values()){
                    System.out.println(epic);
                }
                break;
            case "SUB_TASK":
                for (SubTask subTask: subTaskList.values()){
                    System.out.println(subTask);
                }
        }
        System.out.println("=====".repeat(5));
    }

}
