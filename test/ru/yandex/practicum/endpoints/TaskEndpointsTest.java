package ru.yandex.practicum.endpoints;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.HttpTaskServer;
import ru.yandex.practicum.helpers.GsonHelper;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.*;
import ru.yandex.practicum.services.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TaskEndpointsTest {

    TaskManagerIntf taskManagerTest = Managers.getManager(ManagersType.InMemory);
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(taskManagerTest);
    //    Gson gson = HttpTaskServer.getGson();
    Gson gson;

    @BeforeEach
    public void setUp() throws IOException {

        taskManagerTest.deleteAllElements("TASK");
        taskManagerTest.deleteAllElements("EPICS");
        taskManagerTest.deleteAllElements("SUBTASK");
        taskServer.startServer();
    }

    @AfterEach
    public void shutDown() throws IOException {
        taskServer.stopServer();
    }

    @Test
    void isTaskCreatedTest() throws IOException, InterruptedException {

        Task task = new Task("Test 1", "Testing task 2",
                TaskState.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        // конвертируем её в JSON
        String taskJson = GsonHelper.serializeTask(task);

        // создаём HTTP-клиент и запрос
        try (HttpClient client = HttpClient.newHttpClient();) {

            URI url = URI.create("http://localhost:8080/tasks");

            System.out.println(taskJson);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(201, response.statusCode());
        }
        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = taskManagerTest.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void isTaskFoundNotFoundTest() throws IOException, InterruptedException {

        FillTaskTest.fillTasks(taskManagerTest); //Заполняем все задачи, есть номер 1, и номер 0
        try (HttpClient client = HttpClient.newHttpClient();) {

            URI url = URI.create("http://localhost:8080/tasks/1");

            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/99")).GET().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response1.statusCode());
        }
    }

    @Test
    void isSubTaskUpdatedNotUpdated() throws IOException, InterruptedException {
        FillTaskTest.fillTasks(taskManagerTest); //Заполняем все задачи, есть номер 1, и номер 0
        try (HttpClient client = HttpClient.newHttpClient();) {

            URI url = URI.create("http://localhost:8080/subtasks/");

            SubTask subTask = new SubTask("SubTask1", "Testing update SubTask 2", 3,
                    TaskState.NEW, LocalDateTime.now().plusDays(10).truncatedTo(ChronoUnit.MILLIS), Duration.ofMinutes(5));
            Optional<Integer> optionalSubTaskId = taskManagerTest.createSubTask(subTask);
            subTask.setName("NEW_SUBTASK");
            subTask.setID(optionalSubTaskId.orElse(-1));
            String taskJson = GsonHelper.serializeTask(subTask);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
            if (optionalSubTaskId.isPresent()) {
                int subTaskId = optionalSubTaskId.get();
                String subTaskUpdatedName = taskManagerTest.getSubTaskByID(subTaskId).isPresent() ? taskManagerTest.getSubTaskByID(subTaskId).get().getName() : "";
                assertEquals(subTask.getName(), subTaskUpdatedName);
            }

            SubTask subTask1 = taskManagerTest.getSubTaskByID(5).orElse(null);
            if (subTask1 != null) {

                subTask1.setStartTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS).plusDays(10));
                subTask1.setDuration(Duration.ofMinutes(10));
                taskJson = GsonHelper.serializeTask(subTask1);
                request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                assertEquals(406, response.statusCode());
            }
        }
    }

    @Test
    void isGetTask() throws IOException, InterruptedException {

        FillTaskTest.fillTasks(taskManagerTest); //Заполняем все задачи
        try (HttpClient client = HttpClient.newHttpClient()) {

            URI url = URI.create("http://localhost:8080/tasks/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            List<Task> listTask = GsonHelper.deserializeTasks(response.body());
            assertEquals(2, listTask.size());
        }
    }

    @Test
    void isGetHistory() throws IOException, InterruptedException {

        FillTaskTest.fillTasks(taskManagerTest); //Заполняем все задачи

        try (HttpClient client = HttpClient.newHttpClient()) {


            taskManagerTest.getTaskByID(1);
            taskManagerTest.getTaskByID(2);
            URI url = URI.create("http://localhost:8080/history/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            List<Task> listTask = GsonHelper.deserializeTasks(response.body());
            assertNotNull(listTask);
            assertEquals(2, listTask.size());
        }

    }

    @Test
    void isGetPrioritized() throws IOException, InterruptedException {

        FillTaskTest.fillTasks(taskManagerTest); //Заполняем все задачи

        try (HttpClient client = HttpClient.newHttpClient()) {

            URI url = URI.create("http://localhost:8080/prioritized/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            List<Task> listTask = GsonHelper.deserializeTasks(response.body());
            assertNotNull(listTask);
            ;
            assertFalse(listTask.isEmpty());
        }
    }
}
