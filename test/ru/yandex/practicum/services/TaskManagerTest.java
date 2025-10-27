package ru.yandex.practicum.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.FillTaskTest;
import ru.yandex.practicum.models.ManagersType;

public abstract class TaskManagerTest {

    @Test
    abstract void getTaskByID();

    @Test
    abstract void getEpicByID();

    @Test
    abstract void createEpic();

    @Test
    abstract void createTask();

    @Test
    abstract void createSubtask();

    @Test
    abstract void isUpdatedTaskTest();

    @Test
    abstract void isTimeOverlapFound();
//
//    @Test
//    abstract void updateEpic();
//
//    @Test
//    abstract void updateSubtask();

}
