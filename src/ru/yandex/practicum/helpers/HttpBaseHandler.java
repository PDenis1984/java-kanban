package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Endpoint;
import ru.yandex.practicum.models.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class HttpBaseHandler implements HttpHandler { //Только работа с http - соединения, заголовки, эндпоинты и т.д

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManagerIntf taskManager;

    public HttpBaseHandler(TaskManagerIntf taskManager) {

        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASK: {
                getTask(exchange, 10);
                break;
            }
            case POST_TASK: {
                System.out.println("Run POST TASK ");
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }

    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (pathParts[1]) {

            case "tasks":

                if (pathParts.length == 2 && "GET".equals(requestMethod)) {
                    return Endpoint.GET_TASKS;
                } else if (pathParts.length == 3) {

                    return Endpoint.GET_TASK;
                } else {

                    return Endpoint.POST_TASK;
                }
        }
        //        if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
//            if (requestMethod.equals("POST")) {
//
//                return Endpoint.POST_TASK;
//            }
//            return Endpoint.GET_TASK;
//        } else if (pathParts.length == 3) {
//
//        }
        return Endpoint.UNKNOWN;
    }

    protected void sendText(HttpExchange exchange, String text, int httpCode) {

        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(httpCode, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        } catch (IOException ioException) {
            System.out.println("Произошла ошибка: " + ioException.getMessage());
            sendServerProblem(exchange, "Внутренняя ошибка сервера", 500);
        }
    }

    public void sendNotFound(HttpExchange exchange, String text) {

        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(404, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        } catch (IOException ioException) {
            System.out.println("Произошла ошибка: " + ioException.getMessage());
            sendServerProblem(exchange, "Внутренняя ошибка сервера", 500);
        }
    }

    public void sendHasInteractions(HttpExchange exchange, String text, short httpCode) {

        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(406, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        } catch (IOException ioException) {
            System.out.println("Произошла ошибка: " + ioException.getMessage());
            sendServerProblem(exchange, "Внутренняя ошибка сервера", 500);
        }
    }

    public void sendServerProblem(HttpExchange exchange, String text, int httpCode) {

        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        } catch (IOException ioException) {
            System.out.println("Не удалось отправить ответ, по причине :" + ioException.getMessage());
        }
    }


    //обработчики


    private void getTask(HttpExchange exchange, int mId) {

        Optional<Task> optTask = taskManager.getTaskByID(mId);
        if (optTask.isPresent()) {

            sendText(exchange, GsonHelper.serializeTask(optTask.get()), 200);
        } else {

            sendNotFound(exchange, "Задача с номером " + mId + " не найдена");
        }
    }

    private void getTasks(HttpExchange exchange) {

        List<Task> taskList = taskManager.getAllTasks();
        if (!taskList.isEmpty()) {

            String response = GsonHelper.serializeTasks(taskList);
            sendText(exchange, response, 200);
        } else {

            sendNotFound(exchange, "Задачи с номером не найдены");
        }

    }
}

