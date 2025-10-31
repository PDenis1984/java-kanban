package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Endpoint;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.exceptioons.TaskOverlapException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HttpEpicHandler extends HttpBaseHandler {


    public HttpEpicHandler(TaskManagerIntf taskManager) {

        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Началась обработка Эпиков");
        Endpoint endpoint = EndpointHelper.getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "epics");

        System.out.println("Получился endpoint: " + endpoint.toString());
        switch (endpoint) {
            case GET_EPIC: {

                try {

                    int epicId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
                    getEpic(exchange, epicId);
                } catch (NumberFormatException numberFormatException) {

                    String response = "Переданный идентификатор эпика - не число";
                    System.out.println(response);
                    sendNotFound(exchange, response);
                }
                break;
            }
            case GET_EPICS: {

                getEpics(exchange);
                break;
            }
            case POST_EPIC: {

                String epicString = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                Epic epic = GsonHelper.deserializeEpic(epicString);

                if (epic != null) {
                    if (epic.getID() == null) {
                        createEpic(exchange, epic);
                    } else {
                        updateEpic(exchange, epic);
                    }
                }
                break;
            }

            case DELETE_EPIC: {

                try {
                    int epicId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
                    deleteTask(exchange, epicId, "EPIC");
                    sendText(exchange, "Эпик " + epicId + "Удален", 200);
                } catch (NumberFormatException numberFormatException) {
                    String response = "Переданный идентификатор эпика - не число";
                    sendNotFound(exchange, response);
                }
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
                break;
        }
    }

    private void getEpic(HttpExchange exchange, int mId) {

        Optional<Epic> optEpic = taskManager.getEpicByID(mId);
        if (optEpic.isPresent()) {

            String response = GsonHelper.serializeTask(optEpic.get());
            sendText(exchange, response, 200);
        } else {

            sendNotFound(exchange, "Эпик с номером " + mId + " не найден");
        }
    }

    private void getEpics(HttpExchange exchange) {

        List<Epic> epicList = taskManager.getAllEpic();
        String response = GsonHelper.serializeTasks(epicList);
        sendText(exchange, response, 200);

    }

    public void createEpic(HttpExchange exchange, Epic mEpic) {

        int epicId = taskManager.createEpic(mEpic);
        sendText(exchange, String.valueOf(epicId), 201);
    }


    public void updateEpic(HttpExchange exchange, Epic mEpic) {

        if (taskManager.isTaskExists(mEpic.getID())) {
            try {
                boolean isUpdated = taskManager.updateTask(mEpic);
                if (isUpdated) {
                    sendText(exchange, "Эпик " + mEpic.getID() + " обновлен", 201);
                } else {
                    sendNotFound(exchange, "Эпик " + mEpic.getID() + " не найден");
                }
            } catch (TaskOverlapException taskOverlapException) {
                String response = "Не удалось обновить эпик " + mEpic.getID() + ". Причина? " + taskOverlapException.getMessage();
                System.out.println(response);
                sendHasInteractions(exchange, response);
            }
        } else {
            createEpic(exchange, mEpic);
        }
    }
}


