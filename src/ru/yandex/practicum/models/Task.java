package ru.yandex.practicum.models;


public class Task {
    protected  int ID;
    protected String name;
    protected String description; // Описание может изменяться
    protected TaskState state; // Статус должен изменяться

    public Task( String cName, String cDescription, TaskState cState) {

        this.name = cName;
        this.description = cDescription;
        this.state = cState;
        }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
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
        hash = hash + ((Integer)ID).hashCode();
        return hash; // возвращаем итоговый хеш
    }

    @Override
    public boolean equals(Object obj) {

        return  this.ID == ((Task)obj).ID;
    }

}
