package ru.yandex.practicum;


import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.helpers.*;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.*;
import ru.yandex.practicum.services.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {

    private HttpServer httpServer;
    private final TaskManagerIntf taskManager;
    private static final int HTTP_PORT = 8080;
    private final String bindAddress;

    public HttpTaskServer(TaskManagerIntf taskManager, String newBindAddress) {

        this.taskManager = taskManager;
        if (!"".equals(newBindAddress)) {
            this.bindAddress = newBindAddress;
        } else {
            this.bindAddress = "localhost";
        }
    }

    public HttpTaskServer(TaskManagerIntf taskManager) {

        this.taskManager = taskManager;
        this.bindAddress = "localhost";
    }

    public HttpTaskServer() {

        this.taskManager = Managers.getManager(ManagersType.InMemory);
        this.bindAddress = "localhost";

    }

    public void startServer() throws IOException {

        httpServer = HttpServer.create(new InetSocketAddress(bindAddress, HTTP_PORT), 0);
        httpServer.createContext("/tasks", new HttpBaseHandler(taskManager));
        httpServer.createContext("/epics", new HttpEpicHandler(taskManager));
        httpServer.createContext("/subtasks", new HttpSubTaskHandler(taskManager));
        httpServer.createContext("/prioritized", new HttpPrioritizeHandler(taskManager));
        httpServer.createContext("/history", new HttpHistoryHandler(taskManager));
        httpServer.start();

    }

    public void stopServer() throws IOException {

        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {

        TaskManagerIntf taskManager = Managers.getManager(ManagersType.InMemory);
        fillTasks(taskManager);
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager, "");
        httpTaskServer.startServer();

        System.out.println("endProcess");
        //  httpTaskServer.stopServer();
    }

    public static void fillTasks(TaskManagerIntf taskManager) {

        Task task0 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);

        int task0Id = taskManager.createTask(task0).orElse(-1);
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

        Task task11 = new Task("Починить дверь11", "Вставить глазок и замок11", TaskState.IN_PROGRESS);
        int task11ID = taskManager.createTask(task11).orElse(-1);

        Task task12 = new Task("Починить дверь12", "Вставить глазок и замок12", TaskState.DONE);
        int task12ID = taskManager.createTask(task12).orElse(-1);

    }
}
