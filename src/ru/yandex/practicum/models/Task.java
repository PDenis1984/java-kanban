package ru.yandex.practicum.models;

import java.util.Objects;

public class Task {
    final int ID;
    final String name;
    String description; // писание может изменяться
    TaskState state; // Статус должен изменяться

    public Task(int cID, String cName, String cDescription) {
        this.ID = cID;
        this.name = cName;
        this.description = cDescription;
        this.state = TaskState.NEW; // Все задачи создаются в качестве "новых"
    }


    public TaskState getState() {

        return this.state;
    }

    @Override
    public String toString() {

        String result = "";
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash + ((Integer)ID).hashCode();
        return hash; // возвращаем итоговый хеш
    }
}
