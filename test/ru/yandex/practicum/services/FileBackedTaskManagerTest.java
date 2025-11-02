package ru.yandex.practicum.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest {

    static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss.SSS";
    static TaskManagerIntf fileBackedTaskManager;


    @BeforeEach
    void setUp() {

        fileBackedTaskManager = new FileBackedTaskManager("task.csv");
        FillTaskTest.fillTasks(fileBackedTaskManager);

    }


    @Test
    void isCreatedTest() {


        Epic epic = new Epic("Купить в магазине", "Покупки");
        int epicID = fileBackedTaskManager.createEpic(epic);
        SubTask subTask = new SubTask("Купить пасту", "Покупка 1", epicID, TaskState.IN_PROGRESS);
        int subTaskID = fileBackedTaskManager.createSubTask(subTask).orElse(-1);
        Task task = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int taskID = fileBackedTaskManager.createTask(task).orElse(-1);

        //Все они должны быть в файле.
        // Создаем новый менеджер из того файла, в который сохраняли.
        FileBackedTaskManager checkFileBackedManager = FileBackedTaskManager.loadFromFile(new File("task.csv"));
        assertTrue(checkFileBackedManager.taskList.containsKey(taskID), "Задача не найдена");
        assertTrue(checkFileBackedManager.subTaskList.containsKey(subTaskID), "Подзадача не найдена");
        assertTrue(checkFileBackedManager.epicList.containsKey(epicID), "Эпик не найден");
    }


    @Test
    @Override
    void isUpdatedTaskTest() { //проверяем обновление

        Epic epic = fileBackedTaskManager.getEpicByID(3).orElse(null);

        if (epic != null) {
            epic.setDescription("Сократим текст");
        }
        fileBackedTaskManager.updateEpic(epic);
        FileBackedTaskManager checkFileBackedManager = FileBackedTaskManager.loadFromFile(new File("task.csv"));

        Epic epicCheck = checkFileBackedManager.getEpicByID(3).orElse(null);
        assertEquals(epic, epicCheck, "Эпики не совпадают");

    }


    @Test
    void isDeleteAllElementsTest() { //проверяем удаление в файле, внутри общего удаления вызывается удаление конкретной таски

        fileBackedTaskManager.deleteAllElements("TASK");
        assertTrue(fileBackedTaskManager.getAllTasks().isEmpty(), "Задачи из файла не удалены");

        fileBackedTaskManager.deleteAllElements("EPIC");
        assertTrue(fileBackedTaskManager.getAllEpic().isEmpty(), "Эпики из файла не удалены");

        fileBackedTaskManager.deleteAllElements("SUB_TASK");
        assertTrue(fileBackedTaskManager.getAllSubTasks().isEmpty(), "Подзадачи из файла не удалены");

    }

    @Test
    @Override
    void getTaskByID() {

        Task task1 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int taskID = fileBackedTaskManager.createTask(task1).orElse(-1);
        Task task2 = fileBackedTaskManager.getTaskByID(taskID).orElse(null);
        assertNotNull(task2, "Задача не найдена");
    }

    @Test
    @Override
    void getEpicByID() {

        Epic epic1 = new Epic("Приготовить обед", "Комплексный обед");
        int epic1ID = fileBackedTaskManager.createEpic(epic1);
        Epic epic2 = fileBackedTaskManager.getEpicByID(epic1ID).orElse(null);
        assertNotNull(epic2, "Эпик не найден");
    }


    @Test
    @Override
    void createEpic() {

        Epic epic = new Epic("Приготовить обед", "Комплексный обед");
        int epicID = fileBackedTaskManager.createEpic(epic);
        assertEquals(epic, fileBackedTaskManager.getEpicByID(epicID).orElse(null));
    }

    @Test
    @Override
    void createTask() {

        Task task = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int taskID = fileBackedTaskManager.createTask(task).orElse(-1);
        assertEquals(task, fileBackedTaskManager.getTaskByID(taskID).orElse(null));
    }

    @Test
    @Override
    void createSubtask() {

        Epic epic = new Epic("Приготовить обед", "Комплексный обед");
        int epicID = fileBackedTaskManager.createEpic(epic);
        SubTask subTask = new SubTask("Борщ", "Мясо,  свекла, овощи", epicID, TaskState.NEW);
        int subTaskID = fileBackedTaskManager.createSubTask(subTask).orElse(-1);
        assertEquals(subTask, fileBackedTaskManager.getSubTaskByID(subTaskID).orElse(null));
    }

    @Test
    @Override
    void isTimeOverlapFound() {

        Task task = new Task("Долгая по времени задача", "Долгая длится больше трех дней", TaskState.IN_PROGRESS);
        LocalDateTime startTime = LocalDateTime.parse("2024-10-12 12:10:30.000", DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER));
        task.setStartTime(startTime);
        task.setDuration(Duration.ofMinutes(4320)); //Три дня
        fileBackedTaskManager.createTask(task);

        assertTrue(fileBackedTaskManager.getPrioritizeTasks().contains(task), "Задача не попала в список приоритизации");
        Task task1 = new Task("Задача с пересечением", "Короткая пересекающаяся", TaskState.NEW);
        task1.setStartTime(startTime.plusMinutes(100));
        task1.setDuration(Duration.ofMinutes(20));
        fileBackedTaskManager.updateTask(task1);
        assertFalse(fileBackedTaskManager.getPrioritizeTasks().contains(task1), "Задача не должна быть помещена в списке приоритизации");
    }

    @Test
    void isIOExceptionThrown() {

        assertThrows(IOException.class, () -> {
            BufferedReader bfWriter = new BufferedReader(new FileReader("WrongFileName.txt", StandardCharsets.UTF_8));
        }, "Неверное имя файла");
    }

}
