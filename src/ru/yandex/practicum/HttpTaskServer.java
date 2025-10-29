package ru.yandex.practicum;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.intf.TaskManagerIntf;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final TaskManagerIntf taskManager;
    private static final int HTTP_PORT = 8080;

    private String bindAddress = "http://localhost";

    public HttpTaskServer(TaskManagerIntf taskManager, String newBindAddress) {

        this.taskManager  = taskManager;
        if (!"".equals(newBindAddress)) {
            this.bindAddress = newBindAddress;
        }
    }

    public void  startServer() throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
        httpServer.createContext("/tasks", new PostsHandler(posts));
        httpServer.start();

    }

    public static void main(String[] args) {

    }
}
