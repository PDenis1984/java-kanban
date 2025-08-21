package ru.yandex.practicum.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getManager() {
        InMemoryTaskManager inMTM = Managers.getManager();
        assertNotNull(inMTM, "InMemoryTaskManager Не создан");
;
    }
}