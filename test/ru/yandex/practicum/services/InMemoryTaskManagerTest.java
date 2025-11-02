package ru.yandex.practicum.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest extends TaskManagerTest {

    static final String DATE_TIME_FORMATTER = "yyyy-MM-dd'T'HH:mm:ss.SSS";
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
        int taskID = taskManagerTest.createTask(task1).orElse(-1);
        Task task2 = taskManagerTest.getTaskByID(taskID).orElse(null);
        assertNotNull(task2, "Задача не найдена");
    }

    @Test
    @Override
    void getEpicByID() {

        Epic epic1 = new Epic("Приготовить обед", "Комплексный обед");
        int epic1ID = taskManagerTest.createEpic(epic1);
        Epic epic2 = taskManagerTest.getEpicByID(epic1ID).orElse(null);
        assertNotNull(epic2, "Эпик не найден");
    }


    @Test
    @Override
    void createEpic() {

        Epic epic = new Epic("Приготовить обед", "Комплексный обед");
        int epicID = taskManagerTest.createEpic(epic);
        assertEquals(epic, taskManagerTest.getEpicByID(epicID).orElse(null));
    }

    @Test
    @Override
    void createTask() {

        Task task = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int taskID = taskManagerTest.createTask(task).orElse(-1);
        assertEquals(task, taskManagerTest.getTaskByID(taskID).orElse(null));
    }

    @Test
    @Override
    void createSubtask() {

        Epic epic = new Epic("Приготовить обед", "Комплексный обед");
        int epicID = taskManagerTest.createEpic(epic);
        SubTask subTask = new SubTask("Борщ", "Мясо,  свекла, овощи", epicID, TaskState.NEW);
        int subTaskID = taskManagerTest.createSubTask(subTask).orElse(-1);
        assertEquals(subTask, taskManagerTest.getSubTaskByID(subTaskID).orElse(null));
    }

    @Test
    @Override
    void isUpdatedTaskTest() {

        Task task = taskManagerTest.getTaskByID(1).orElse(null);
        if (task != null) {

            task.setDescription("Сократим текст");
        }
        taskManagerTest.updateTask(task);
        Task taskCheck = taskManagerTest.getTaskByID(1).orElse(null);
        assertEquals(task, taskCheck, "Эпики не совпадают");
    }

    @Test
    @Override
    void isTimeOverlapFound() {

    }
}