package ru.yandex.practicum.models;

public class SubTask extends Task {
    int epicID; // Эпик может измениться

    public SubTask(int cID, String cName, String cDescription, int cEpicID) {
        super(cID, cName, cDescription);
        this.epicID = cEpicID;
    }

    public int changeEpic(int mID) {
        this.epicID = mID;
        return this.epicID;
    }

    @Override
    public String toString() {

        String result = "";
        result = "Подзадача номер: [" + this.getID() + "], Наименование: '" + this.getName()
                + "', Описание '" + this.description + "' находится в статусе: '" + this.getState()
                + "'; Подзадача относится к эпику: [" + this.epicID + "]";
        return result;
    }
}
