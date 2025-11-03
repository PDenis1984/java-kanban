package ru.yandex.practicum;

import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskState;

public class Main {


    public static void fillTasks(TaskManagerIntf taskManager) {

        Task task0 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);


        Task task1 = new Task("Починить дверь", "Вставить глазок и замок", TaskState.NEW);
        int task1ID = taskManager.createTask(task1).orElse(-1);

        Epic epic2 = new Epic("Подготовка к зачету", "Выучить необходимые параграфы, решить задачи, написать шпаргалки");
        int epic2ID = taskManager.createEpic(epic2);

        SubTask subtask3 = new SubTask("Изучение параграфа 1", "Страница 1", epic2.getID(), TaskState.NEW);
        int subTask3ID = taskManager.createSubTask(subtask3).orElse(-1);

        SubTask subTask4 = new SubTask("Решить задачу", "Задача  номер 2", epic2ID, TaskState.IN_PROGRESS);
        int subTask4ID = taskManager.createSubTask(subTask4).orElse(-1);

        SubTask subTask5 = new SubTask("Решить интеграл", "Интеграл номер 3", epic2ID, TaskState.IN_PROGRESS);
        int subTask5ID = taskManager.createSubTask(subTask5).orElse(-1);


        Epic epic6 = new Epic("Приготовить обед", "Комплексный обед");
        int epic6ID = taskManager.createEpic(epic6);

    }
}





