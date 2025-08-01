package ru.yandex.practicum.models;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subTaskElements;

    public Epic(int cID, String cName, String cDescription) {
        super(cID, cName, cDescription);
        subTaskElements = new ArrayList<Integer>();
    }

    public int addSubTask(SubTask mSubTask) {

        this.subTaskElements.add(mSubTask.ID);
        recountEpicState();
        return mSubTask.ID;
    }

    private void recountEpicState() {

        TaskState taskState = this.state;
        int allState = subTaskElements.size();
        int nState = 0;
        int inState = 0;
        int dState = 0;

        for (int subTask : subTaskElements) {
            if (subTask.state == TaskState.NEW) {
                nState++;
            } else if (subTask.state == TaskState.IN_PROGRESS) {
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
}
