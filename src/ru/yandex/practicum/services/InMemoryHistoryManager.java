package ru.yandex.practicum.services;

import ru.yandex.practicum.intf.HistoryManagerIntf;
import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManagerIntf {

    private List<Task> historyList;

    public InMemoryHistoryManager() {
        this.historyList = new ArrayList<>();
    }
    @Override
    public void add(Task task) {

        if (historyList.size() == 10) {
            historyList.removeFirst();
        }
        historyList.addLast(task);
    }

    public List<Task> getHistory(){

        return  historyList;
    }
}

