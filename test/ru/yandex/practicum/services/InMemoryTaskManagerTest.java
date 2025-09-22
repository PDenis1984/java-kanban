package ru.yandex.practicum.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest {

    static TaskManagerIntf taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = Managers.getManager(ManagersType.InMemory);
        FillTaskTest.fillTasks(taskManagerTest);
    }

    @Test
    void getTaskByID() {

        Task task1 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int taskID = taskManagerTest.createTask(task1);
        Task task2 = taskManagerTest.getTaskByID(taskID);
        assertNotNull(task1, "Задача не найдена");
    }

    @Test
    void getEpicByID() {

        Epic epic1 = new Epic("Приготовить обед", "Комплексный обед");
        int epic1ID = taskManagerTest.createEpic(epic1);
        Epic epic2 = taskManagerTest.getEpicByID(epic1ID);
        assertNotNull(epic2, "Эпик не найден");
    }


    @Test
    void createEpic() {

        Epic epic = new Epic("Приготовить обед", "Комплексный обед");
        int epicID = taskManagerTest.createEpic(epic);
        assertEquals(epic, taskManagerTest.getEpicByID(epicID));
    }

    @Test
    void createTask() {

        Task task = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int taskID = taskManagerTest.createTask(task);
        assertEquals(task, taskManagerTest.getTaskByID(taskID));
    }

    @Test
    void createSubtask() {

        Epic epic = new Epic("Приготовить обед", "Комплексный обед");
        int epicID = taskManagerTest.createEpic(epic);
        SubTask subTask = new SubTask("Борщ", "Мясо,  свекла, овощи", epicID, TaskState.NEW);
        int subTaskID = taskManagerTest.createSubTask(subTask);
        assertEquals(subTask, taskManagerTest.getSubTaskByID(subTaskID));
    }
}