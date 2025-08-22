package ru.yandex.practicum.services;

import ru.yandex.practicum.intf.HistoryManagerIntf;
import ru.yandex.practicum.intf.TaskManagerIntf;

public class Managers {

    public static TaskManagerIntf getManager() {

        return new InMemoryTaskManager();
    }

    public static HistoryManagerIntf getDefaultHistoryManager(){

        return  new InMemoryHistoryManager();
    }
}
