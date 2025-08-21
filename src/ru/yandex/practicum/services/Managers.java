package ru.yandex.practicum.services;

public class Managers {

    public static InMemoryTaskManager getManager() {

        return new InMemoryTaskManager();
    }
}
