package ru.yandex.practicum.services;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.intf.TaskManagerIntf;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getManager() {
        TaskManagerIntf inMTM = Managers.getManager();
        assertNotNull(inMTM, "InMemoryTaskManager Не создан");
;
    }
}