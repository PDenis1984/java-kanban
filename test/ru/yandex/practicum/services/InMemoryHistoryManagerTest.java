package ru.yandex.practicum.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.models.FillTaskTest;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskState;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    static InMemoryTaskManager taskManagerTest;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = Managers.getManager();
        FillTaskTest.fillTasks(taskManagerTest);
    }

    @Test
    void notOverMaxCapacity(){
        Task task1 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int task1ID = taskManagerTest.createTask(task1);
        for (int i = 0; i < 11 ; i++) {
            taskManagerTest.getTaskByID(task1ID);
        }
        assertEquals(10, taskManagerTest.getHistory().size());
    }

    @Test
    void getHistoryTest (){

        List<Task> taskList = taskManagerTest.getHistory();
        assertEquals(0, taskList.size());
        Task task1 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        int task1ID = taskManagerTest.createTask(task1);
        taskManagerTest.getTaskByID(task1ID);
        taskList = taskManagerTest.getHistory();
        assertFalse(taskList.isEmpty());
    }

    @Test
    void addTest() {
        InMemoryHistoryManager inHM = new InMemoryHistoryManager();
        Task task1 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);
        inHM.add(task1);
        List<Task> taskList = taskManagerTest.getHistory();
        assertNotNull(taskList, "Список пуст и не проинициализирован");
        assertEquals(1, taskList.size(), "Список пуст!");
        inHM.add(task1);
        assertEquals(inHM.getHistory().get(0), inHM.getHistory().get(1), "Задачи не совпадают");
    }

}