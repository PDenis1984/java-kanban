package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Endpoint;

import java.io.IOException;

public class HttpEpicHandler extends HttpBaseHandler{


    public HttpEpicHandler(TaskManagerIntf taskManager) {

        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = EndpointHelper.getEndpoint(
                exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "epic");

        switch (endpoint) {
            case GET_EPIC: {
                getEpic(exchange, 10);
                break;
            }
            case POST_EPIC: {
                System.out.println("Run POST EPIC ");
                break;
            }
            case GET_EPICS: {
                System.out.println("run get All epics");
                break;
            }
            case DELETE_EPIC: {

                System.out.println("Run delete epics");
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private void  getEpic(HttpExchange exchange, int epicId) {


    }
}
