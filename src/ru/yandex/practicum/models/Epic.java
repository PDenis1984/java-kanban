package ru.yandex.practicum.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    ArrayList<Integer> subTaskElements;

    public Epic(int cID, String cName, String cDescription) {
        super(cID, cName, cDescription);
        subTaskElements = new ArrayList<Integer>();
    }



    public void updateState(HashMap<Integer, SubTask> subTaskHashMap) {
        this.recountEpicState(subTaskHashMap);
    }
    protected void recountEpicState(HashMap<Integer, SubTask> subTaskHashMap) {

        TaskState taskState = this.state;
        int allState = subTaskElements.size();
        int nState = 0;
        int inState = 0;
        int dState = 0;

        for (int subTask : subTaskElements) {
            if (subTaskHashMap.get(subTask).state == TaskState.NEW) {
                nState++;
            } else if (subTaskHashMap.get(subTask).state == TaskState.IN_PROGRESS) {
                inState++;
            } else {
                dState++;
            }
        }
        if (nState == allState) {
            this.state = TaskState.NEW;
        } else if (dState == allState) {
            this.state = TaskState.DONE;
        } else {
            this.state = TaskState.IN_PROGRESS;
        }
    }

    public void  removeSubtask(int mID) {

    }

    @Override
    public String toString() {

        String result = "Эпик номер: " + this.ID + ", Наименование: '" + this.name
                + "', Описание " + this.description + " находится в статусе: '" + this.state
                + "' ; Подзадачи в эпике: " + subTaskElements.toString();
        return result;
    }
}
