package ru.yandex.practicum.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    ArrayList<Integer> subTaskElements;

    public Epic(int cID, String cName, String cDescription) {
        super(cID, cName, cDescription);
        subTaskElements = new ArrayList<Integer>();
    }


    protected void recountEpicState(HashMap<Integer, SubTask> subTaskHashMap) {

        TaskState taskState = this.getState();
        int allState = subTaskElements.size();
        int nState = 0;
        int inState = 0;
        int dState = 0;

        for (int subTask : subTaskElements) {
            if (subTaskHashMap.get(subTask).getState() == TaskState.NEW) {
                nState++;
            } else if (subTaskHashMap.get(subTask).getState() == TaskState.IN_PROGRESS) {
                inState++;
            } else {
                dState++;
            }
        }
        if (nState == allState) {
            this.setState(TaskState.NEW);;
        } else if (dState == allState) {
            this.setState(TaskState.DONE);
        } else {
            this.setState(TaskState.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {

        String result = "Эпик номер: [" + this.getID() + "], Наименование: '" + this.getName()
                + "', Описание: '" + this.description + "' находится в статусе: '" + this.getState()
                + "'; Подзадачи в эпике: " + subTaskElements;
        return result;
    }
}
