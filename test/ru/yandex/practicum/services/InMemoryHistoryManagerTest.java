package ru.yandex.practicum.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.HistoryManagerIntf;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    static TaskManagerIntf taskManagerTest;
    static HistoryManagerIntf inHM;

    @BeforeAll
    static void beforeAll() {

        taskManagerTest = Managers.getManager();
        FillTaskTest.fillTasks(taskManagerTest);
        inHM = new InMemoryHistoryManager();
    }

    @Test
    void getHistoryTest() {

        HistoryManagerIntf historyManager = new InMemoryHistoryManager();
        historyManager.clearHistory();
        List<Task> taskList;
        taskList = taskManagerTest.getHistory();
        assertFalse(taskList.isEmpty());
    }

    @Test
    void addTest() {


        Task task1 = new Task("Сходить в магазин", "За хлебом", TaskState.IN_PROGRESS);

        int task1ID = taskManagerTest.createTask(task1);
        taskManagerTest.getTaskByID(task1ID);
        List<Task> taskList = taskManagerTest.getHistory();
        assertNotNull(taskList, "Список пуст и не проинициализирован!");
        assertFalse(taskList.isEmpty(), "Список пуст!");
        taskManagerTest.getTaskByID(task1ID);
        assertEquals(1, taskManagerTest.getHistory().size(), "Задача перезаписана");
    }

    @Test
    void notRepeatedTaskAndEpicsAndSubTask() {

        Task task1 = new Task("Абсолютно новый таск", "новый таск", TaskState.IN_PROGRESS);
        inHM.add(task1);
        int taskListSizeBefore = inHM.getHistory().size();
        //повторяем запись
        inHM.add(task1);
        int taskListSizeAfter = inHM.getHistory().size();
        assertEquals(taskListSizeBefore, taskListSizeAfter, "В список попал дубль таска");

        Epic epic1 = new Epic("Абсолютно новый эпик", "новый эпик");
        inHM.add(epic1);
        int epicListSizeBefore = inHM.getHistory().size();
        inHM.add(epic1);
        int epicListSizeAfter = inHM.getHistory().size();
        assertEquals(epicListSizeBefore, epicListSizeAfter, "В список попал дубль эпика");

        SubTask subtask1 = new SubTask("Абсолютно новый сабтаск", "новый сабтаск", epic1.getID(), TaskState.NEW);
        inHM.add(subtask1);
        int subTaskListSizeBefore = inHM.getHistory().size();
        inHM.add(subtask1);
        int subTaskListSizeAfter = inHM.getHistory().size();
        assertEquals(subTaskListSizeBefore, subTaskListSizeAfter, "В список попал дубль сабтаска");
    }

    @Test
    void deleteEpicWithHisHistory() {


        Epic epic1 = new Epic("Абсолютно новый эпик", "новый эпик");
        inHM.add(epic1);
        List<Task> listEpics = inHM.getHistory();
        assertTrue(inHM.getHistory().contains(epic1), "Эпик не попал в список");
        inHM.remove(epic1.getID());
        assertFalse(inHM.getHistory().contains(epic1), "Эпик не удалился из списка");
    }

    @Test
    void deleteEpicWithSubtasks() {

        Epic epic1 = new Epic("Подготовка к зачету", "Выучить необходимые параграфы, решить задачи, написать шпаргалки");
        int epic1ID = taskManagerTest.createEpic(epic1);

        SubTask subTask1 = new SubTask("Изучение параграфа 1", "Страница 1", epic1.getID(), TaskState.NEW);
        int subTask1ID = taskManagerTest.createSubtask(subTask1);

        SubTask subTask2 = new SubTask("Решить задачу", "Задача  номер 2", epic1ID, TaskState.IN_PROGRESS);
        int subTask2ID = taskManagerTest.createSubtask(subTask2);

        SubTask subTask3 = new SubTask("Решить интеграл", "Интеграл номер 3", epic1ID, TaskState.IN_PROGRESS);
        int subTask3ID = taskManagerTest.createSubtask(subTask3);

        taskManagerTest.getEpicByID(epic1ID);
        taskManagerTest.getSubTaskByID(subTask1ID);
        taskManagerTest.getSubTaskByID(subTask2ID);
        taskManagerTest.getSubTaskByID(subTask3ID);
        assertTrue(taskManagerTest.getHistory().contains(epic1), "Эпик не попал в историю");
        assertTrue(taskManagerTest.getHistory().contains(subTask1), "Сабтаск не попал в историю");
        assertTrue(taskManagerTest.getHistory().contains(subTask2), "Сабтаск не попал в историю");
        assertTrue(taskManagerTest.getHistory().contains(subTask3), "Сабтаск не попал в историю");

        taskManagerTest.deleteElement(epic1ID, "EPIC");
        assertFalse(taskManagerTest.getHistory().contains(epic1), "Эпик  не удален из истории");
        assertFalse(taskManagerTest.getHistory().contains(subTask1), "Сабтаск не удален из истории");
        assertFalse(taskManagerTest.getHistory().contains(subTask2), "Сабтаск не удален из истории");
        assertFalse(taskManagerTest.getHistory().contains(subTask3), "Сабтаск не удален из истории");

    }
}