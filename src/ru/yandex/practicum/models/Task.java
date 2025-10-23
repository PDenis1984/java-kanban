package ru.yandex.practicum.models;


import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected int iD;
    protected String name;
    protected String description; // Описание может изменяться
    protected TaskState state; // Статус должен изменяться
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String cName, String cDescription, TaskState cState) {

        this.name = cName;
        this.description = cDescription;
        this.state = cState;
    }

    public int getID() {

        return this.iD;
    }

    public void setID(int iD) {

        this.iD = iD;
    }

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }


    public TaskState getState() {

        return state;
    }

    public void setState(TaskState mState) {

        this.state = mState;
    }

    public LocalDateTime getStartTime() {

        return this.startTime;
    }

    public void SetStartTime(LocalDateTime starTime) {

        this.startTime = starTime;
    }

    public LocalDateTime getEndTime() {

        return this.startTime.plusMinutes(duration.toMinutes());
    }


    @Override
    public String toString() {

        String result = "";
        result = "Задача номер: [" + this.getID() + "], Наименование: '" + this.getName()
                + "', Описание: '" + this.getDescription() + "' находится в статусе: '" + this.getState() + "'";
        return result;
    }


    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash + ((Integer) iD).hashCode();
        return hash; // возвращаем итоговый хеш
    }

    @Override
    public boolean equals(Object obj) {

        return this.getID() == ((Task) obj).iD;
    }

}
