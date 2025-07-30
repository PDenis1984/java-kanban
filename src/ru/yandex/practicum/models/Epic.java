package ru.yandex.practicum.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends  Task{
    HashMap<Long, SubTask> subTasks;
    public  Epic(Long cID, String cName, String cDescription){
        super(cID, cName,cDescription);
        subTasks  = new HashMap<Long, SubTask>();
    }
}
