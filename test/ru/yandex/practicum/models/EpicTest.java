package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.services.InMemoryTaskManager;
import ru.yandex.practicum.services.Managers;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    static InMemoryTaskManager taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = Managers.getManager();
        FillTaskTest.fillTasks(taskManagerTest);
    }
    @Test
    void testEquals() {

        Epic epic1 = taskManagerTest.getEpicByID(1);
        Epic epic2 = taskManagerTest.getEpicByID(1);
        assertEquals(epic1, epic2);
    }

    @Test
    void canAddedEpicToHimself() {

        Epic epic1 = taskManagerTest.getEpicByID(1);
        SubTask subTask = taskManagerTest.getSubTaskByID(3);
        epic1.addSubTask(subTask.getID());
        boolean ifContains = epic1.getAllSubTask().contains(epic1.getID());
        assertFalse(ifContains);
    }

}