package ru.yandex.practicum;

import ru.yandex.practicum.models.*;

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        int newValue = 1;
        fillTasks(taskManager);
    }

    public static void fillTasks(TaskManager taskManager) {

        //TТест номер 1, добавление
        Epic epic1 = new Epic(TaskManager.getElementID(), "Подготовка к зачету", "Выучить необходимые параграфы, решить задачи, написать шпаргалки");
        taskManager.createEpic(epic1);

        SubTask subtask1 = new SubTask(TaskManager.getElementID(), "Изучение параграфа 1", "Страницы с 5 по 12", epic1.getID());
        taskManager.createSubtask(epic1.getID(), subtask1);
        SubTask subTask2 = new SubTask(TaskManager.getElementID(), "Решить задачу", "Задачи, номер 2, 5", epic1.getID());
        taskManager.createSubtask(epic1.getID(), subTask2);
        Epic epic2 = new Epic(TaskManager.getElementID(), "Приготовить обед", "Комплексный обед");
        taskManager.createEpic(epic2);
        SubTask subTask21 = new SubTask(TaskManager.getElementID(), "Борщ", "Мясо,  свекла, овощи", epic2.getID());

        taskManager.createSubtask(epic2.getID(), subTask21);

        Task task1 = new Task(TaskManager.getElementID(), "Сходить в магазин", "За хлебом");
        taskManager.createTask(task1);
        Task task2 = new Task(TaskManager.getElementID(), "Починить дверь", "Вставить глазок и замок");
        taskManager.createTask(task2);

        System.out.println("Добавление всех типов задач: \r\n");
        TaskManager.printAllElements("TASK");
        TaskManager.printAllElements("EPIC");
        TaskManager.printAllElements("SUB_TASK");


        //Тест номер  2 - Изменение


        int epicID1 = 1;
        int epicID4 = 4;
        int taskID6 = 6;
        int subTaskID2 = 2;
        int subTaskID3 = 3;
        int taskID7 = 7;
        int taskID12 = 12;

        epic1.setDescription("Изучить необходимые параграфы");
        taskManager.updateEpic(epicID1, epic1);

        epic2.setDescription("Бизнес-ланч");
        taskManager.updateEpic(epicID4, epic2);

        subTask2.setDescription("Задача 10");
        taskManager.updateSubTask(subTaskID3, subTask2);

        task2.setDescription(task2.getDescription() + " и ручку");
        taskManager.updateTask(taskID7, task2);


        System.out.println("\r\nИзменение всех типов задач: \r\n");
        TaskManager.printAllElements("TASK");
        TaskManager.printAllElements("EPIC");
        TaskManager.printAllElements("SUB_TASK");

        // Тест номер 3, удаление. Удаление Эпика, удаление задачи, удаление подзадачи, попытка удаления с неверным номером
        TaskManager.deleteElement(epicID1, "EPIC");
        TaskManager.deleteElement(taskID6, "TASK");
        TaskManager.deleteElement(subTaskID2, "SUB_TASK"); //Эпик уже удален, подзадача удаляется
        TaskManager.deleteElement(taskID12, "TASK"); //передаем неверный номер подзадачи

        System.out.println("\r\nУдаление выбранных типов задач:" + "\r\n".repeat(1));
        TaskManager.printAllElements("TASK");
        TaskManager.printAllElements("EPIC");
        TaskManager.printAllElements("SUB_TASK");


        //Удаление всех задач
        TaskManager.deleteAllElements("TASK");
        TaskManager.deleteAllElements("EPIC");
        TaskManager.deleteAllElements("SUB_TASK");

        System.out.println("Удаление всех задач");
        TaskManager.printAllElements("TASK");
        TaskManager.printAllElements("EPIC");
        TaskManager.printAllElements("SUB_TASK");
    }


}

