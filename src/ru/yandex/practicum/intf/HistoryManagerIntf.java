package ru.yandex.practicum.intf;

import ru.yandex.practicum.models.Task;

import java.util.List;

public interface HistoryManagerIntf {

    List<Task> getHistory();

    void add(Task task);

    void clearHistory();

    void remove(int mId);
}
