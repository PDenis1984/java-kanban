package ru.yandex.practicum.models;

import java.util.HashMap;

public class Task {
    final Long ID;
    final String name;
    String description;


    public Task(Long cID, String cName, String cDescription) {
        this.ID = cID;
        this.name = cName;
        this.description = cDescription;
    }
}
