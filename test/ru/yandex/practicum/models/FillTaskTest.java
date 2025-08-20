package ru.yandex.practicum.models;

import ru.yandex.practicum.services.InMemoryTaskManager;

public class FillTaskTest {

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

    }
}