package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.services.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {

    static TaskManagerIntf taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = Managers.getManager(ManagersType.InMemory);
        FillTaskTest.fillTasks(taskManagerTest);
    }

    @Test
    void equalsTest() {

        SubTask subTask1 = taskManagerTest.getSubTaskByID(2).orElse(null);
        SubTask subTask2 = taskManagerTest.getSubTaskByID(2).orElse(null);
        assertEquals(subTask1, subTask2, "Не совпадают");
    }
}