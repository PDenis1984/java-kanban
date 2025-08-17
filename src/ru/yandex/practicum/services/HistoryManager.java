package ru.yandex.practicum.services;

import ru.yandex.practicum.models.Task;

import java.util.Deque;

public class HistoryManager<T> {

    final TaskManager taskManager;
    final Deque<T> historyQueue;
    public HistoryManager(TaskManager cTaskManager) {
        this.taskManager = cTaskManager;
        this.historyQueue = new
    }

    public<T extends Task>  getHistory() {


    }
}
