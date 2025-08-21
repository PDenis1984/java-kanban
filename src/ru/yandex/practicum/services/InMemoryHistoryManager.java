package ru.yandex.practicum.services;

import ru.yandex.practicum.intf.HistoryManagerIntf;
import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManagerIntf {

    private List<Task> historyList;
    private final static int MAX_HISTORY = 10;

    public InMemoryHistoryManager() {
        this.historyList = new ArrayList<>();
    }
    @Override
    public void add(Task task) {

        if (historyList.size() == MAX_HISTORY) {
            historyList.removeFirst();
        }
        historyList.addLast(task);
    }

    public List<Task> getHistory(){

        return  new ArrayList<>(this.historyList);
    }
}

