package ru.yandex.practicum.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {

    static TaskManagerIntf fileBackedTaskManager;


    @BeforeEach
    void setUp() {

        fileBackedTaskManager = new FileBackedTaskManager("task.csv");
        FillTaskTest.fillTasks(fileBackedTaskManager);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void isCreatedTest() {

        Epic epic = new Epic("Купить в магазине", "Покупки");
        int epicID = fileBackedTaskManager.createEpic(epic);
        SubTask subTask = new SubTask("Купить пасту", "Покупка 1", epicID, TaskState.IN_PROGRESS);
        int subTaskID = fileBackedTaskManager.createSubTask(subTask);
        Task task = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int taskID = fileBackedTaskManager.createTask(task);

        //все они должны быть в файле
        // Создаем новый менеджер из того файла, в который сохраняли
        FileBackedTaskManager checkFileBackedManager = FileBackedTaskManager.loadFromFile(new File("task.csv"));
        assertTrue(checkFileBackedManager.taskList.containsKey(taskID), "Задача не найдена");
        assertTrue(checkFileBackedManager.subTaskList.containsKey(subTaskID), "Подзадача не найдена");
        assertTrue(checkFileBackedManager.epicList.containsKey(epicID), "Эпик не найден");
    }


    @Test
    void isUpdatedTaskTest() { //проверяем обновление

        Epic epic = fileBackedTaskManager.getEpicByID(3);
        epic.setDescription("Сократим текст");
        fileBackedTaskManager.updateEpic(epic);
        FileBackedTaskManager checkFileBackedManager = FileBackedTaskManager.loadFromFile(new File("task.csv"));

        Epic epicCheck = checkFileBackedManager.getEpicByID(3);
        assertEquals(epic, epicCheck, "Эпики не совпадают");

    }


    @Test
    void deleteAllElementsTest() { //проверяем удаление в файле, внутри общего удаления вызывается удаление конкретной таски

        fileBackedTaskManager.deleteAllElements("TASK");
        assertTrue(fileBackedTaskManager.getAllTasks().isEmpty(), "Задачи из файла не удалены");

        fileBackedTaskManager.deleteAllElements("EPIC");
        assertTrue(fileBackedTaskManager.getAllEpic().isEmpty(), "Эпики из файла не удалены");

        fileBackedTaskManager.deleteAllElements("SUB_TASK");
        assertTrue(fileBackedTaskManager.getAllSubTasks().isEmpty(), "Подзадачи из файла не удалены");

    }


}