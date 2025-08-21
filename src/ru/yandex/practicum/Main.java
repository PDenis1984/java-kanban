package ru.yandex.practicum;

import ru.yandex.practicum.models.*;
import ru.yandex.practicum.services.InMemoryTaskManager;
import ru.yandex.practicum.services.Managers;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        InMemoryTaskManager taskManager = Managers.getManager();
        fillTasks(taskManager);
    }

    public static void fillTasks(InMemoryTaskManager taskManager) {

        //Тест номер 1, добавление
        Epic epic1 = new Epic("Подготовка к зачету", "Выучить необходимые параграфы, решить задачи, написать шпаргалки");
        int epic1ID = taskManager.createEpic(epic1);

        SubTask subtask1 = new SubTask("Изучение параграфа 1", "Страницы с 5 по 12", epic1.getID(), TaskState.NEW);

        int subTask1ID = taskManager.createSubtask(subtask1);
        SubTask subTask2 = new SubTask("Решить задачу", "Задачи, номер 2, 5", epic1ID, TaskState.IN_PROGRESS);
        int subTask2ID = taskManager.createSubtask(subTask2);

        Epic epic2 = new Epic("Приготовить обед", "Комплексный обед");
        int epic2ID = taskManager.createEpic(epic2);
        SubTask subTask21 = new SubTask("Борщ", "Мясо,  свекла, овощи", epic2ID, TaskState.NEW);

        int subTask21ID = taskManager.createSubtask(subTask21);

        Task task1 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int task1ID = taskManager.createTask(task1);
        Task task2 = new Task("Починить дверь", "Вставить глазок и замок", TaskState.NEW);
        int task2ID = taskManager.createTask(task2);

        System.out.println("Добавление всех типов задач: \r\n");
        taskManager.printAllElements("TASK");
        taskManager.printAllElements("EPIC");
        taskManager.printAllElements("SUB_TASK");


        //Тест номер  2 - Изменение


        int epicID1 = 1;
        int epicID4 = 4;
        int taskID6 = 6;
        int subTaskID2 = 2;
        int subTaskID3 = 3;
        int taskID7 = 7;
        int taskID12 = 12;

        epic1.setDescription("Изучить необходимые параграфы");
        taskManager.updateEpic(epic1);

        epic2.setDescription("Бизнес-ланч");
        taskManager.updateEpic(epic2);

        subTask2.setDescription("Задача 10");
        taskManager.updateSubTask(subTask2);

        task2.setDescription(task2.getDescription() + " и ручку");
        taskManager.updateTask(task2);


        System.out.println("\r\nИзменение всех типов задач: \r\n");
        taskManager.printAllElements("TASK");
        taskManager.printAllElements("EPIC");
        taskManager.printAllElements("SUB_TASK");


        //Test 3 Просмотр задач, эпиков подзадач, с достижением 11 просмотров
        System.out.println("Задача      6: " + taskManager.getTaskByID(6));
        System.out.println("Эпик        1: " + taskManager.getEpicByID(1));
        System.out.println("Подзадача   3: " + taskManager.getSubTaskByID(3));
        System.out.println(taskManager.getHistory());
        System.out.println("Запрашиваем 10 раз одну  задачу");
        for (int i = 0; i < 10; i++) {
            taskManager.getTaskByID(7);
        }
        System.out.println(taskManager.getHistory());

        // Тест номер 3, удаление. Удаление Эпика, удаление задачи, удаление подзадачи, попытка удаления с неверным номером
        taskManager.deleteElement(epicID1, "EPIC");
        taskManager.deleteElement(taskID6, "TASK");
        taskManager.deleteElement(subTaskID2, "SUB_TASK"); //Эпик уже удален, подзадача удаляется
        taskManager.deleteElement(taskID12, "TASK"); //передаем неверный номер подзадачи

        System.out.println("\r\nУдаление выбранных типов задач:" + "\r\n".repeat(1));
        taskManager.printAllElements("TASK");
        taskManager.printAllElements("EPIC");
        taskManager.printAllElements("SUB_TASK");




        //Удаление всех задач
        taskManager.deleteAllElements("TASK");
        taskManager.deleteAllElements("EPIC");
        taskManager.deleteAllElements("SUB_TASK");

        System.out.println("Удаление всех задач");
        taskManager.printAllElements("TASK");
        taskManager.printAllElements("EPIC");
        taskManager.printAllElements("SUB_TASK");
    }
}





