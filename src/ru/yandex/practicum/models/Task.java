package ru.yandex.practicum.models;


public class Task {
    final int ID;
    final String name;
    String description; // писание может изменяться
    TaskState state; // Статус должен изменяться

    public Task(int cID, String cName, String cDescription) {
        this.ID = cID;
        this.name = cName;
        this.description = cDescription;
        this.state = TaskState.NEW; // Все задачи создаются в качестве "новых"
    }


    TaskState getState() {

        return this.state;
    }

    public void updateState() {

        if (this.state == TaskState.NEW) {
            this.state = TaskState.IN_PROGRESS;
        } else if(this.state == TaskState.IN_PROGRESS) {
            this.state = TaskState.DONE;
        }
    }
    public int getID(){
        return this.ID;
    }

    @Override
    public String toString() {

        String result = "";
        result = "Задача номер:" + this.ID + ", Наименование: '" + this.name
                + "', Описание: '" + this.description + "' находится в статусе: '" + this.state + "'";
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash + ((Integer)ID).hashCode();
        return hash; // возвращаем итоговый хеш
    }

    @Override
    public boolean equals(Object obj) {

        return  this.ID == ((Task)obj).ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(TaskState state) {
        this.state = state;
    }
}
