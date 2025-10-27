package ru.yandex.practicum.models.exceptioons;

public class TaskOverlapException extends RuntimeException {

    public TaskOverlapException(final String message) {
        super(message);
    }
}
