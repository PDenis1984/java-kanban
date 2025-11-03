package ru.yandex.practicum.models;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task {
    protected Integer iD;
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

    public Task(String cName, String cDescription, TaskState cState, LocalDateTime cStartTime, Duration cDuration) {

        this.name = cName;
        this.description = cDescription;
        this.state = cState;
        this.startTime = cStartTime.truncatedTo(ChronoUnit.MILLIS);
        this.duration = cDuration;
    }

    public Integer getID() {

        return this.iD;
    }

    public void setID(Integer iD) {

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

    public void setStartTime(LocalDateTime starTime) {

        this.startTime = starTime.truncatedTo(ChronoUnit.MILLIS);
    }

    public LocalDateTime getEndTime() {

        if (this.getStartTime() != null) {
            return this.startTime.plusMinutes(duration.toMinutes());
        } else {
            return null;
        }
    }

    public void setDuration(Duration mDuration) {

        this.duration = mDuration;
    }


    public Duration getDuration() {

        return this.duration;
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

        return Objects.equals(this.getID(), ((Task) obj).iD);
    }

}
