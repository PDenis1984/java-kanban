package ru.yandex.practicum.services;

import ru.yandex.practicum.intf.HistoryManagerIntf;
import ru.yandex.practicum.intf.TaskManagerIntf;
import ru.yandex.practicum.models.ManagersType;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskState;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Managers {

    public static TaskManagerIntf getManager(ManagersType managersType) {

        switch (managersType) {
            case InMemory:

                return new InMemoryTaskManager();
            case InFile:

                return  new FileBackedTaskManager("tasks.txt");
            default:

                return new InMemoryTaskManager();
        }

    }

    public static HistoryManagerIntf getDefaultHistoryManager() {

        return new InMemoryHistoryManager();
    }

}
