package ru.yandex.practicum.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private ArrayList<Integer> subTaskElements;

    public Epic(String cName, String cDescription) {
        super(cName, cDescription,TaskState.NEW);
        subTaskElements = new ArrayList<Integer>();

    }


    protected void deleteSubTaskByID(int mID){

        if (subTaskElements.contains(Integer.valueOf(mID))){
            subTaskElements.remove(Integer.valueOf(mID));
        } else {
            System.out.println("В этом эпике нет такой подзадачи");
        }
    }

    protected void deleteAllSubTask (){

        subTaskElements.clear();;
    }

    protected void addSubTask(int mSubTaskID) {

        subTaskElements.add(mSubTaskID);
    }

    protected ArrayList<Integer> getAllSubTaskByEpicID(int epicID) {
        return  this.subTaskElements;
    }

    @Override
    public String toString() {

        String result = "Эпик номер: [" + this.getID() + "], Наименование: '" + this.getName()
                + "', Описание: '" + this.getDescription() + "' находится в статусе: '" + this.getState()
                + "'; Подзадачи в эпике: " + subTaskElements;
        return result;
    }
}
