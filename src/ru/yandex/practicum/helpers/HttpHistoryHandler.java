package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Endpoint;
import ru.yandex.practicum.models.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpHistoryHandler extends HttpBaseHandler {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private TaskManagerIntf inMemoryTaskManager;

    public HttpHistoryHandler(TaskManagerIntf inMemoryTaskManager) {
        super();
        this.inMemoryTaskManager = inMemoryTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Началась обработка истории");
        Endpoint endpoint = EndpointHelper.getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "history");
        switch (endpoint) {
            case GET_HISTORY:
                getHistory(exchange);
                break;
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
                break;
        }
    }

    private void getHistory(HttpExchange exchange) {

        List<Task> historyList = inMemoryTaskManager.getHistory();
        String response = GsonHelper.serializeTasks(historyList);
        sendText(exchange, response, 200);
    }
}
