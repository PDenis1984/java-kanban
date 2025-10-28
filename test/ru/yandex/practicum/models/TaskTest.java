package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.services.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    static InMemoryTaskManager taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = new InMemoryTaskManager();
        FillTaskTest.fillTasks(taskManagerTest);
    }

    @Test
    void testEquals() {
        Task task1 = taskManagerTest.getTaskByID(6).orElse(null);
        Task task2 = taskManagerTest.getTaskByID(6).orElse(null);
        assertEquals(task1, task2);
    }
}