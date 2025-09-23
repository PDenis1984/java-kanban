package ru.yandex.practicum.services;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.ManagersType;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    void getManager() {
        TaskManagerIntf inMTM = Managers.getManager(ManagersType.InMemory);
        assertNotNull(inMTM, "InMemoryTaskManager Не создан");
        ;
    }
}