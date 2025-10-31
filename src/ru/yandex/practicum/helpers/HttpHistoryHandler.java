package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.intf.HistoryManagerIntf;
import ru.yandex.practicum.models.Endpoint;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpHistoryHandler extends HttpBaseHandler {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HistoryManagerIntf historyManager;

    public HttpHistoryHandler(HistoryManagerIntf historyManager) {
        super();
        this.historyManager = historyManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Началась обработка Истории");
        getHistory(exchange);

    }

    private void getHistory(HttpExchange exchange) {

        List<Task> historyList = historyManager.getHistory();
        String response = GsonHelper.serializeTasks(historyList);
        sendText(exchange, response, 200);
    }
}
