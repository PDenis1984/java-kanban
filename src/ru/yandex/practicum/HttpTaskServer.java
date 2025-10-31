package ru.yandex.practicum;


import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.helpers.HttpBaseHandler;
import ru.yandex.practicum.helpers.HttpEpicHandler;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.*;
import ru.yandex.practicum.services.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final TaskManagerIntf taskManager;
    private static final int HTTP_PORT = 8080;
    private final String bindAddress;

    public HttpTaskServer(TaskManagerIntf taskManager, String newBindAddress) {


        this.taskManager  = taskManager;
        if (!"".equals(newBindAddress)) {
            this.bindAddress = newBindAddress;
        } else {
            this.bindAddress = "localhost";
        }

    }

    private HttpServer  startServer() throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(bindAddress, HTTP_PORT), 0);
        httpServer.createContext("/tasks", new HttpBaseHandler(taskManager));
        httpServer.createContext("/epics", new HttpEpicHandler(taskManager));
        httpServer.createContext("/subtasks", new HttpBaseHandler(taskManager));
        httpServer.start();
        return httpServer;

    }

    private void stopServer(HttpServer httpServer) throws IOException {

        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {

        TaskManagerIntf taskManager = Managers.getManager(ManagersType.InMemory);
        fillTasks(taskManager);
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager,"");
        HttpServer httpServer = httpTaskServer.startServer();

        System.out.println("endProcess");
        //httpTaskServer.stopServer(httpServer);
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

    }
}
