package ru.yandex.practicum.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicID;

    public SubTask(String cName, String cDescription, int cEpicID, TaskState cState) {

        super(cName, cDescription, cState);
        this.epicID = cEpicID;
    }

    public SubTask(String cName, String cDescription, int cEpicID, TaskState cState, LocalDateTime cStartTime, Duration cDuration) {

        super(cName, cDescription, cState, cStartTime, cDuration);
        this.epicID = cEpicID;
    }

    @Override
    public String toString() {

        String result = "";
        result = "Подзадача номер: [" + this.getID() + "], Наименование: '" + this.getName()
                + "', Описание '" + this.description + "' находится в статусе: '" + this.getState()
                + "'; Подзадача относится к эпику: [" + this.epicID + "]";
        return result;
    }


    public int getEpicID() {
        return epicID;
    }
}
