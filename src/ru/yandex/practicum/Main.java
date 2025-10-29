package ru.yandex.practicum;

import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskState;
import ru.yandex.practicum.services.FileBackedTaskManager;

import java.io.File;

public class Main {

//    public static void main(String[] args) {
//
//        System.out.println("Поехали!");
//        FileBackedTaskManager taskManager = new FileBackedTaskManager("tasks.txt");
//        taskManager = FileBackedTaskManager.loadFromFile(new File("tasks.txt"));
//        taskManager.save();
//        fillTasks(taskManager);
//
//        System.out.println("Task1 : "  + taskManager.getTaskByID(1).toString());
//        System.out.println("Task2 : "  + taskManager.getTaskByID(2).toString());
//        System.out.println("Epic3 : "  + taskManager.getEpicByID(3).toString());
//        System.out.println("SubTask4 : "  + taskManager.getSubTaskByID(4).toString());
//        System.out.println("SubTask5 : "  + taskManager.getSubTaskByID(5).toString());
//        System.out.println("Epic6 : "  + taskManager.getSubTaskByID(6).toString());
//
//
//
//    }

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





