package ru.yandex.practicum.models.exceptioons;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException (final String message) {
        super(message);
    }
}
