package ru.yandex.practicum;

import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskManager;


public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        int newValue = 1;
        fillTasks();
    }

    public static  void fillTasks() {
        Long lID = TaskManager.getElementID();
//        TaskManager.epicList.put(lID, new Task(lD, "Уборка", "прибраться в квартире"));
//
//        lID = TaskManager.getElementID();
//        TaskManager.epicList.put(lID, new Task(lD, "Готовка", "Приготовить еды"));
//        lID = TaskManager.getElementID();
//        TaskManager.epicList.put(lID, new Task(lD, "Учеба", "Сделать уроки"));

    }


}

