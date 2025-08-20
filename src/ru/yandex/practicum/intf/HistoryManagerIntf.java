package ru.yandex.practicum.intf;

import ru.yandex.practicum.models.Task;

import java.util.List;

public interface HistoryManagerIntf {

    public List<Task> getHistory();

    public void add(Task task);
}
