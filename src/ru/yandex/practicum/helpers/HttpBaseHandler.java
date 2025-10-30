package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Endpoint;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.ManagersType;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.services.Managers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class HttpBaseHandler implements HttpHandler { //Только работа с http - соединения, заголовки, эндпоинты и т.д

    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final TaskManagerIntf taskManager;

    public HttpBaseHandler(TaskManagerIntf taskManager) {

        this.taskManager = taskManager;
    }

    public HttpBaseHandler() {

        this.taskManager = Managers.getManager(ManagersType.InMemory);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = EndpointHelper.getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "tasks");

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

            sendNotFound(exchange, "Задачи не найдены");
        }
    }

    public void createTask(HttpExchange exchange, Task mTask) {

        Optional<Integer> optionalTaskId = taskManager.createTask(mTask);
        if (optionalTaskId.isPresent()) {
            sendText(exchange, optionalTaskId.get().toString(), 201);
        } else {
            sendServerProblem(exchange, "Не удалось создать задачу", 500);
        }
    }

    public void updateTask(HttpExchange exchange, Task mTask) {

        boolean isUpdated = taskManager.updateTask(mTask);
        if (isUpdated) {
            sendText(exchange, "Задача "+ mTask.getID() + " обновлена",  200);
        } else {
            sendNotFound(exchange, "Задача " + mTask.getID() + " не найдена");
        }
    }

    public void createEpic(Epic mEpic) {


    }
}

