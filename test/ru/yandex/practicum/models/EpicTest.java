package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.services.Managers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EpicTest {

    static TaskManagerIntf taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = Managers.getManager(ManagersType.InMemory);
        FillTaskTest.fillTasks(taskManagerTest);
    }

    @Test
    void testEquals() {

        Epic epic1 = taskManagerTest.getEpicByID(3).orElse(null);
        Epic epic2 = taskManagerTest.getEpicByID(3).orElse(null);
        assertEquals(epic1, epic2);
    }

    @SuppressWarnings("checkstyle:WhitespaceAround")
    @Test
    void canAddedEpicToHimself() {

        Optional<Epic> optionalEpic = taskManagerTest.getEpicByID(3);
        Optional<SubTask> subTask = taskManagerTest.getSubTaskByID(4);
        if (subTask.isPresent()){
            if (optionalEpic.isPresent()){
                Epic epic1 = optionalEpic.get();
                epic1.addSubTask(subTask.get().getID());
                boolean ifContains = epic1.getAllSubTask().contains(epic1.getID());
                assertFalse(ifContains);
            }

        }


    }

}