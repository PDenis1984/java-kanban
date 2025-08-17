package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.services.TaskManager;

class EpicTest {

    static TaskManager taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = new TaskManager();
        FillTaskTest.fillTasks(taskManagerTest);
    }
    @Test
    void deleteSubTaskByID() {


    }
}