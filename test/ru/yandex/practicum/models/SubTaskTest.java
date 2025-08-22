package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.services.InMemoryTaskManager;
import ru.yandex.practicum.services.Managers;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    static TaskManagerIntf taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = Managers.getManager();
        FillTaskTest.fillTasks(taskManagerTest);
    }

    @Test
    void equalsTest() {

        SubTask subTask1 = taskManagerTest.getSubTaskByID(2);
        SubTask subTask2 = taskManagerTest.getSubTaskByID(2);
        assertEquals(subTask1, subTask2, "Не совпадают");
    }
}