package ru.yandex.practicum.models;

import java.util.HashMap;

public class TaskManager {

    static int elementID = 0;
    static HashMap<Long, Task> taskList;
    static HashMap<Long, Epic> epicList;

    public TaskManager() {
        taskList = new HashMap<Long, Task>();
        epicList = new HashMap<Long, Epic>();
    }

    public static Task getTaskByID(Long cID) {

        return taskList.get(cID);
    }

    public static Epic getEpicByID(Long cID) {

        return epicList.get(cID);
    }

    public static SubTask getSubTaskByID(Long cID) {

        for (Epic epic: epicList.values()) {
            if (epic.subTasks.get(cID)!=null) {
                return  epic.subTasks.get(cID);
            }
        }
        return  null;
    }

}
