package ru.yandex.practicum.models;

public class SubTask extends  Task{
    Long epicID;

    public SubTask(Long cID, String cName, String cDescription, Long cEpicID){
        super(cID, cName,cDescription);
        this.epicID = cEpicID;
    }
}
