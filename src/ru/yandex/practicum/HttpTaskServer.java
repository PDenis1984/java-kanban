package ru.yandex.practicum;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.helpers.HttpBaseHandler;
import ru.yandex.practicum.helpers.HttpEpicHandler;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.ManagersType;
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
        } else{
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
    public static void main(String[] args) throws IOException{

        TaskManagerIntf taskManager = Managers.getManager(ManagersType.InMemory);
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager,"");
        HttpServer httpServer = httpTaskServer.startServer();

        httpTaskServer.stopServer(httpServer);
    }
}
