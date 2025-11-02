package ru.yandex.practicum.models;

import ru.yandex.practicum.intf.TaskManagerIntf;

import java.time.Duration;
import java.time.LocalDateTime;

public class FillTaskTest {

    public static void fillTasks(TaskManagerIntf taskManager) {

        // Дополнительное задание, добавляем по шаблону:
        // Сперва две задачи
        // Эпик и три подзадачи
        // Эпик без подзадач
        Task task0 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS, LocalDateTime.now(), Duration.ofMinutes(20));
        int task0ID = taskManager.createTask(task0).orElse(-1);
        Task task1 = new Task("Починить дверь", "Вставить глазок и замок", TaskState.NEW, LocalDateTime.now().plusMinutes(25), Duration.ofMinutes(15));
        int task1ID = taskManager.createTask(task1).orElse(-1);

        Epic epic2 = new Epic("Подготовка к зачету", "Выучить необходимые параграфы, решить задачи, написать шпаргалки");
        int epic2ID = taskManager.createEpic(epic2);

        SubTask subtask3 = new SubTask("Изучение параграфа 1", "Страница 1", epic2.getID(),
                TaskState.NEW, LocalDateTime.now().plusHours(2), Duration.ofMinutes(5));
        int subTask3ID = taskManager.createSubTask(subtask3).orElse(-1);

        SubTask subTask4 = new SubTask("Решить задачу", "Задача  номер 2", epic2ID,
                TaskState.IN_PROGRESS, LocalDateTime.now().plusHours(1), Duration.ofMinutes(20));
        int subTask4ID = taskManager.createSubTask(subTask4).orElse(-1);

        SubTask subTask5 = new SubTask("Решить интеграл", "Интеграл номер 3", epic2ID,
                TaskState.IN_PROGRESS, LocalDateTime.now().plusMinutes(50), Duration.ofMinutes(10));
        int subTask5ID = taskManager.createSubTask(subTask5).orElse(-1);


        Epic epic6 = new Epic("Приготовить обед", "Комплексный обед");


    }
}