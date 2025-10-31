package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.ManagersType;
import ru.yandex.practicum.services.Managers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpPrioritizeHandler extends HttpBaseHandler {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManagerIntf taskManager;

    public HttpPrioritizeHandler(TaskManagerIntf taskManager) {
        super();
        this.taskManager = taskManager;
    }

    public HttpPrioritizeHandler() {

        this.taskManager = Managers.getManager(ManagersType.InMemory);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {


    }

}


