package ru.yandex.practicum.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public final class Epic extends Task {
    private ArrayList<Integer> subTaskElements;
    private LocalDateTime endTime;

    public Epic(String cName, String cDescription) {
        super(cName, cDescription, TaskState.NEW);
        subTaskElements = new ArrayList<Integer>();

    }


    public void deleteSubTaskByID(int mID) {

        if (subTaskElements.contains(Integer.valueOf(mID))) {
            subTaskElements.remove(Integer.valueOf(mID));
        } else {
            System.out.println("В этом эпике нет такой подзадачи");
        }
    }

    public void deleteAllSubTask() {

        subTaskElements.clear();
        ;
    }

    public void addSubTask(int mSubTaskID) {

        if (!(this.getID() == mSubTaskID)) {
            subTaskElements.add(mSubTaskID);
        }
    }

    public ArrayList<Integer> getAllSubTask() {
        return new ArrayList<>(subTaskElements); //
    }

    @Override
    public String toString() {

        String result = "Эпик номер: [" + this.getID() + "], Наименование: '" + this.getName()
                + "', Описание: '" + this.getDescription() + "' находится в статусе: '" + this.getState()
                + "'; Подзадачи в эпике: " + subTaskElements;
        return result;
    }

    public LocalDateTime getEndTime() {

        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {

        this.endTime = endTime;
    }
}
