package ru.yandex.practicum.services;

public class Managers<T> {

    public InMemoryTaskManager getManager() {

        return new InMemoryTaskManager();
    }
}
