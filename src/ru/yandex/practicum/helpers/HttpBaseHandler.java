package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Endpoint;
import ru.yandex.practicum.models.ManagersType;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.exceptioons.TaskOverlapException;
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


        System.out.println("Началась обработка запроса");
        Endpoint endpoint = EndpointHelper.getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "tasks");

        System.out.println("Получился endpoint: " + endpoint.toString());
        switch (endpoint) {
            case GET_TASK: {

                try {

                    int taskId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
                    getTask(exchange, taskId);
                } catch (NumberFormatException numberFormatException) {

                    String response = "Переданный идентификатор задачи - не число";
                    System.out.println(response);
                    sendNotFound(exchange, response);
                }
                break;
            }
            case GET_TASKS: {

                getTasks(exchange);
                break;
            }
            case POST_TASK: {

                String taskString = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                Task task = GsonHelper.deserializeTask(taskString);
                try {
                    if (task != null) {
                        if (task.getID() == null) {
                            createTask(exchange, task);
                        } else {
                            updateTask(exchange, task);
                        }
                    }
                } catch (TaskOverlapException taskOverlapException) {

                    String response = "Задача пересекается по времени с другими задачами или подзадачами";
                    System.out.println(response);
                    sendHasInteractions(exchange, response);
                }
                break;
            }

            case DELETE_TASK: {

                try {
                    int taskId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
                    deleteTask(exchange, taskId, "TASK");
                    sendText(exchange, "Задача " + taskId + "Удалена", 200);
                } catch (NumberFormatException numberFormatException) {
                    String response = "Переданный идентификатор задачи - не число";
                    sendNotFound(exchange, response);
                }
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
                break;
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
            sendServerProblem(exchange, "Внутренняя ошибка сервера");
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
            sendServerProblem(exchange, "Внутренняя ошибка сервера");
        }
    }

    public void sendHasInteractions(HttpExchange exchange, String text) {

        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(406, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        } catch (IOException ioException) {
            System.out.println("Произошла ошибка: " + ioException.getMessage());
            sendServerProblem(exchange, "Внутренняя ошибка сервера");
        }
    }

    public void sendServerProblem(HttpExchange exchange, String text) {

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

            String response = GsonHelper.serializeTask(optTask.get());
            sendText(exchange, response, 200);
        } else {

            sendNotFound(exchange, "Задача с номером " + mId + " не найдена");
        }
    }

    private void getTasks(HttpExchange exchange) {

        List<Task> taskList = taskManager.getAllTasks();
        String response = GsonHelper.serializeTasks(taskList);
        sendText(exchange, response, 200);

    }

    public void createTask(HttpExchange exchange, Task mTask) {

        try {
            Optional<Integer> optionalTaskId = taskManager.createTask(mTask);
            if (optionalTaskId.isPresent()) {
                sendText(exchange, optionalTaskId.get().toString(), 201);
            } else {
                sendServerProblem(exchange, "Не удалось создать задачу");
            }
        } catch (TaskOverlapException taskOverlapException) {
            String response = "Не удалось создать задачу: " + mTask.toString() + " .Причина: " + taskOverlapException.getMessage();
            System.out.println();
            sendHasInteractions(exchange, response);
        }
    }

    public void updateTask(HttpExchange exchange, Task mTask) {

        if (taskManager.isTaskExists(mTask.getID())) {
            try {
                boolean isUpdated = taskManager.updateTask(mTask);
                if (isUpdated) {
                    sendText(exchange, "Задача " + mTask.getID() + " обновлена", 201);
                } else {
                    sendNotFound(exchange, "Задача " + mTask.getID() + " не найдена");
                }
            } catch (TaskOverlapException taskOverlapException) {
                String response = "Не удалось обновить задачу " + mTask.getID() + ". Причина? " + taskOverlapException.getMessage();
                System.out.println(response);
                sendHasInteractions(exchange, response);
            }
        } else {
            createTask(exchange, mTask);
        }
    }


    public void deleteTask(HttpExchange exchange, int mTaskId, String mType) {

        if (taskManager.isSubTaskExists(mTaskId) || taskManager.isTaskExists(mTaskId) || taskManager.isEpicExists(mTaskId)) {
            try {
                taskManager.deleteElement(mTaskId, mType);
            } catch (Exception exception) {
                exception.printStackTrace();
                sendServerProblem(exchange, "Не удалось удалить задачу : " + mTaskId + exception.getMessage());
            }

        } else {
            sendNotFound(exchange, "Задача " + mTaskId + " не найдена");

        }
    }
}

