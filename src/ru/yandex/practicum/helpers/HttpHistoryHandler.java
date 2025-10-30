package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.intf.HistoryManagerIntf;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpHistoryHandler extends HttpBaseHandler {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HistoryManagerIntf historyManager;

    public HttpHistoryHandler (HistoryManagerIntf historyManager) {
        super();
        this.historyManager = historyManager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {



    }

}
