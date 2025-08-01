package ru.yandex.practicum.models;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    static int elementID; // Сквозная нумерация задач
    static HashMap<Integer, Task> taskList;
    static HashMap<Integer, Epic> epicList;
    static HashMap<Integer, SubTask subTaskList;

    public TaskManager() {
        taskList = new HashMap<Integer, Task>();
        epicList = new HashMap<Integer, Epic>();
        subTaskList = new HashMap<Integer, SubTask>();
        elementID = 0;

    }

    // Набор для получения задач
    public static Task getTaskByID(int mID) {
        if (!taskList.isEmpty()) {
            return taskList.get(mID);
        }
        return null;
    }

    public static Epic getEpicByID(int mID) {

        if (!epicList.isEmpty()) {
            return epicList.get(mID);
        }
        return null;
    }

    public static SubTask getSubTaskByID(int mID) { //передаем именно SubTaskID

        if (!subTaskList.isEmpty()) {
            return subTaskList.get(mID);
        }
        return null;
    }


    public static void addEpic(String mName, String mDescription) {

        int eID = getElementID();
        Epic epic = new Epic(eID, mName, mDescription);
        epicList.put(eID, epic);
    }

    public static void addTask(String mName, String mDescritopn) {

        int tID = getElementID();
        Task task = new Task(tID, mName, mDescritopn);
        taskList.put(tID, task);
    }

    public static void addSubtask(int mEpicID, SubTask subTask) {

        if ( getEpicByID(mEpicID) != null) {
            subTaskList.put(subTask.ID , subTask);
            getEpicByID(mEpicID).subTaskElements.add(subTask.ID);
        } else {
            System.out.println("Указан неверный номер Эпика");
        }
    }


    // Удаление
    public static void deleteTask(int mID, TaskType mType) {

        switch (mType) {
            case EPIC:
                epicList.remove(mID);
                break;
            case TASK:
                taskList.remove(mID);
                break;
            case SUB_TASK:
                subTaskList.remove(mID);

                break;
            default:
                System.out.println("Передан неверный тип задачи");
        }
    }

    public static int getElementID() {
        elementID++;
        return elementID;
    }

    public static void updatedTask(int mID, String mName, String description) { // ID обязательное, но должно быть хотя бы одно из ...

        //HashMap<int, Task> task =

    }

}
