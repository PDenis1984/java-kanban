package ru.yandex.practicum.helpers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.Endpoint;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.exceptioons.TaskOverlapException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HttpSubTaskHandler extends HttpBaseHandler {


    public HttpSubTaskHandler(TaskManagerIntf taskManager) {

        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Началась обработка Подзадач");
        Endpoint endpoint = EndpointHelper.getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "subtasks");

        System.out.println("Получился endpoint: " + endpoint.toString());
        switch (endpoint) {
            case GET_SUBTASK: {

                try {

                    int subTaskId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
                    getSubTask(exchange, subTaskId);
                } catch (NumberFormatException numberFormatException) {

                    String response = "Переданный идентификатор Подзадачи - не число";
                    System.out.println(response);
                    sendNotFound(exchange, response);
                }
                break;
            }
            case GET_SUBTASKS: {

                getSubTasks(exchange);
                break;
            }
            case POST_SUBTASK: {

                String subTaskString = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                SubTask subTask = GsonHelper.deserializeSubTask(subTaskString);

                if (subTask != null) {
                    if (subTask.getID() == null) {
                        createSubTask(exchange, subTask);
                    } else {
                        updateSubTask(exchange, subTask);
                    }
                }
                break;
            }

            case DELETE_SUBTASK: {

                try {
                    int subTaskId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
                    deleteTask(exchange, subTaskId, "SUBTASK");
                    sendText(exchange, "Подзадача " + subTaskId + " Удалена", 200);
                } catch (NumberFormatException numberFormatException) {
                    String response = "Переданный идентификатор подзадачи - не число";
                    sendNotFound(exchange, response);
                }
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
                break;
        }
    }

    private void getSubTask(HttpExchange exchange, int mId) {

        Optional<SubTask> optionalSubTask = taskManager.getSubTaskByID(mId);
        if (optionalSubTask.isPresent()) {

            String response = GsonHelper.serializeTask(optionalSubTask.get());
            sendText(exchange, response, 200);
        } else {

            sendNotFound(exchange, "Подзадача с номером " + mId + " не найдена");
        }
    }

    private void getSubTasks(HttpExchange exchange) {

        List<SubTask> subTasksList = taskManager.getAllSubTasks();
        String response = GsonHelper.serializeTasks(subTasksList);
        sendText(exchange, response, 200);

    }

    public void createSubTask(HttpExchange exchange, SubTask mSubTask) {

        try {
            Optional<Integer> optionalSubTaskId = taskManager.createSubTask(mSubTask);
            if (optionalSubTaskId.isPresent()) {
                sendText(exchange, optionalSubTaskId.get().toString(), 201);
            } else {
                sendServerProblem(exchange, "Не удалось создать подзадачу");
            }
        } catch (TaskOverlapException taskOverlapException) {
            String response = "Не удалось создать подзадачу: " + mSubTask.toString() + " .Причина: " + taskOverlapException.getMessage();
            sendHasInteractions(exchange, response);
        }
    }

    public void updateSubTask(HttpExchange exchange, SubTask mSubTask) {

        System.out.println("обновление сабтаска");

        if (taskManager.isSubTaskExists(mSubTask.getID())) {

            try {
                boolean isUpdated = taskManager.updateSubTask(mSubTask);
                if (isUpdated) {
                    sendText(exchange, "Подзадача " + mSubTask.getID() + " обновлена", 201);
                } else {
                    sendNotFound(exchange, "Подзадача " + mSubTask.getID() + " не найдена");
                }
            } catch (TaskOverlapException taskOverlapException) {
                String response = "Не удалось обновить Подзадачу " + mSubTask.getID() + ". Причина: " + taskOverlapException.getMessage();
                System.out.println(response);
                sendHasInteractions(exchange, response);
            }
        } else {
            createSubTask(exchange, mSubTask);
        }
    }


}
