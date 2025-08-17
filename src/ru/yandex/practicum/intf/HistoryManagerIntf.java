package ru.yandex.practicum.intf;

import ru.yandex.practicum.models.Task;

public interface HistoryManagerIntf {

    public <T extends Task> T getHistory();
}
