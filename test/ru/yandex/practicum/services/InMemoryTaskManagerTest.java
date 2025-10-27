package ru.yandex.practicum.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest extends TaskManagerTest {

    static final String DATE_TIME_FORMATTER = "dd.MM.yyyy HH:mm:ss";
    static TaskManagerIntf taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = Managers.getManager(ManagersType.InMemory);
        FillTaskTest.fillTasks(taskManagerTest);
    }
    @Test
    @Override
    void getTaskByID() {

        Task task1 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int taskID = taskManagerTest.createTask(task1);
        Task task2 = taskManagerTest.getTaskByID(taskID);
        assertNotNull(task1, "Задача не найдена");
    }

    @Test
    @Override
    void getEpicByID() {

        Epic epic1 = new Epic("Приготовить обед", "Комплексный обед");
        int epic1ID = taskManagerTest.createEpic(epic1);
        Epic epic2 = taskManagerTest.getEpicByID(epic1ID);
        assertNotNull(epic2, "Эпик не найден");
    }


    @Test
    @Override
    void createEpic() {

        Epic epic = new Epic("Приготовить обед", "Комплексный обед");
        int epicID = taskManagerTest.createEpic(epic);
        assertEquals(epic, taskManagerTest.getEpicByID(epicID));
    }

    @Test
    @Override
    void createTask() {

        Task task = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int taskID = taskManagerTest.createTask(task);
        assertEquals(task, taskManagerTest.getTaskByID(taskID));
    }

    @Test
    @Override
    void createSubtask() {

        Epic epic = new Epic("Приготовить обед", "Комплексный обед");
        int epicID = taskManagerTest.createEpic(epic);
        SubTask subTask = new SubTask("Борщ", "Мясо,  свекла, овощи", epicID, TaskState.NEW);
        int subTaskID = taskManagerTest.createSubTask(subTask);
        assertEquals(subTask, taskManagerTest.getSubTaskByID(subTaskID));
    }
    @Test
    @Override
    void isUpdatedTaskTest() {

        Epic epic = taskManagerTest.getEpicByID(3);
        epic.setDescription("Сократим текст");
        taskManagerTest.updateEpic(epic);
        FileBackedTaskManager checkFileBackedManager = FileBackedTaskManager.loadFromFile(new File("task.csv"));

        Epic epicCheck = checkFileBackedManager.getEpicByID(3);
        assertEquals(epic, epicCheck, "Эпики не совпадают");
    }

    @Test
    @Override
    void  isTimeOverlapFound() {

    }
}