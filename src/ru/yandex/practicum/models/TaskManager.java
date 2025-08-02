package ru.yandex.practicum.models;

import java.util.HashMap;

public class TaskManager {

    static int elementID; // Сквозная нумерация задач
    static HashMap<Integer, Task> taskList;
    static HashMap<Integer, Epic> epicList;
    static HashMap<Integer, SubTask> subTaskList;

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

    static SubTask getSubTaskByID(int mID) { //передаем именно SubTaskID

        if (!subTaskList.isEmpty()) {
            return subTaskList.get(mID);
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
    public static void createEpic(Epic mEpic) {

        epicList.put(mEpic.ID, mEpic);
    }

    public static void createTask(Task task) {

        taskList.put(task.ID, task);
    }

    public static void createSubtask(int mEpicID, SubTask subTask) {

        Epic epic = getEpicByID(mEpicID);
        if (epic != null) {
            subTaskList.put(subTask.ID, subTask);
            epic.subTaskElements.add(subTask.ID);
            epic.recountEpicState(subTaskList);

        } else {
            System.out.println("Указан неверный номер Эпика");
        }
    }


    // Удаление
    static void deleteElement(int mID, String mType) {

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
                    System.out.println("Передан неверный номер Задачи");
                }
                break;
            case "SUB_TASK":

                if (subTaskList.containsKey(mID)) {
                    subTaskList.remove(mID);
                    Epic epic = epicList.get(mID);
                    epic.subTaskElements.remove(mID);
                    epic.recountEpicState(subTaskList);
                } else {
                    System.out.println("Передан неверный номер Подзадачи");
                }
                break;
            default:
                System.out.println("Передан неверный тип задачи");
        }
    }

    static void deleteAllElements(String mType) {

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
                    epic.state = TaskState.NEW;
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
    public static void updateEpic(int epicID, Epic epic) {

        epicList.put(epicID, epic);
        epic.recountEpicState(subTaskList);
    }

    public static void updateTask(int taskID, Task task) {

        taskList.put(taskID, task);
    }

    public static void updateSubTask(int subTaskID, SubTask subTask) {

        subTaskList.put(subTaskID, subTask);
        Epic epic = getEpicBySubtaskID(subTaskID);
        subTask.updateState();
        epic.recountEpicState(subTaskList);
    }

    public static void printAllElements(String mType) {

        System.out.println("=====".repeat(5));
        switch (mType) {
            case "TASK":
                for (Task task: taskList.values().toArray(new Task[0])){
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
