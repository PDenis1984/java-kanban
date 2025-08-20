package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.services.InMemoryTaskManager;

class EpicTest {

    static InMemoryTaskManager taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = new InMemoryTaskManager();
        FillTaskTest.fillTasks(taskManagerTest);
    }
    @Test
    void deleteSubTaskByID() {


    }
}