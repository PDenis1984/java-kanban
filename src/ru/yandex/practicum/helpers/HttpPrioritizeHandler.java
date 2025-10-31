package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.ManagersType;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.services.Managers;

import java.io.IOException;
import java.util.List;


public class HttpPrioritizeHandler extends HttpBaseHandler {

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

        System.out.println("Началась обработка Списка Задач с приоритетами");
        getPrioritize(exchange);

    }

    private void getPrioritize(HttpExchange exchange) {

        List<Task> prioritizeList = taskManager.getPrioritizeTasks();
        String response = GsonHelper.serializeTasks(prioritizeList);
        sendText(exchange, response, 200);
    }
}


