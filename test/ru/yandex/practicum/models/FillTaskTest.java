package ru.yandex.practicum.models;

import ru.yandex.practicum.intf.TaskManagerIntf;

public class FillTaskTest {

    public static void fillTasks(TaskManagerIntf taskManager) {

        // Дополнительное задание, добавляем по шаблону:
        // Сперва две задачи
        // Эпик и три подзадачи
        // Эпик без подзадач
        Task task0 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int task0ID = taskManager.createTask(task0);
        Task task1 = new Task("Починить дверь", "Вставить глазок и замок", TaskState.NEW);
        int task1ID = taskManager.createTask(task1);

        Epic epic2 = new Epic("Подготовка к зачету", "Выучить необходимые параграфы, решить задачи, написать шпаргалки");
        int epic2ID = taskManager.createEpic(epic2);

        SubTask subtask3 = new SubTask("Изучение параграфа 1", "Страница 1", epic2.getID(), TaskState.NEW);
        int subTask3ID = taskManager.createSubTask(subtask3);

        SubTask subTask4 = new SubTask("Решить задачу", "Задача  номер 2", epic2ID, TaskState.IN_PROGRESS);
        int subTask4ID = taskManager.createSubTask(subTask4);

        SubTask subTask5 = new SubTask("Решить интеграл", "Интеграл номер 3", epic2ID, TaskState.IN_PROGRESS);
        int subTask5ID = taskManager.createSubTask(subTask5);


        Epic epic6 = new Epic("Приготовить обед", "Комплексный обед");


    }
}