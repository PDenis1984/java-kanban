package ru.yandex.practicum.models;

import ru.yandex.practicum.intf.TaskManagerIntf;

public class FillTaskTest {

    public static void fillTasks(TaskManagerIntf taskManager) {

        // Дополнгительное задание, добавляем по шаблону:
        // Сперва две задачи
        // Эпик и три подзадачи
        // Эпик без подзадач
        Task task1 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int task1ID = taskManager.createTask(task1);
        Task task2 = new Task("Починить дверь", "Вставить глазок и замок", TaskState.NEW);
        int task2ID = taskManager.createTask(task2);

        Epic epic1 = new Epic("Подготовка к зачету", "Выучить необходимые параграфы, решить задачи, написать шпаргалки");
        int epic1ID = taskManager.createEpic(epic1);

        SubTask subtask1 = new SubTask("Изучение параграфа 1", "Страница 1", epic1.getID(), TaskState.NEW);
        int subTask1ID = taskManager.createSubtask(subtask1);

        SubTask subTask2 = new SubTask("Решить задачу", "Задача  номер 2", epic1ID, TaskState.IN_PROGRESS);
        int subTask2ID = taskManager.createSubtask(subTask2);

        SubTask subTask3 = new SubTask("Решить интеграл", "Интеграл номер 3", epic1ID, TaskState.IN_PROGRESS);
        int subTask3ID = taskManager.createSubtask(subtask1);


        Epic epic2 = new Epic("Приготовить обед", "Комплексный обед");


    }
}