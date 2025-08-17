package ru.yandex.practicum.services;

public class Managers<T> {

    public  TaskManager getManager() {

        return new TaskManager();
    }
}
