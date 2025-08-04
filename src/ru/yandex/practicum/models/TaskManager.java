package ru.yandex.practicum.models;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private  static int elementID; // Сквозная нумерация задач
    private  HashMap<Integer, Task> taskList;
    private  HashMap<Integer, Epic> epicList;
    private  HashMap<Integer, SubTask> subTaskList;

    public TaskManager() {
        taskList = new HashMap<Integer, Task>();
        epicList = new HashMap<Integer, Epic>();
        subTaskList = new HashMap<Integer, SubTask>();
        elementID = 0;

    }

    // Набор для получения задач
     Task getTaskByID(int mID) {

        if (!taskList.isEmpty()) {
            return taskList.get(mID);
        }
        return null;
    }

     Epic getEpicByID(int mID) {

        if (!epicList.isEmpty()) {
            return epicList.get(mID);
        }
        return null;
    }


     Epic getEpicBySubtaskID(int mSubtaskID) {

        for (Epic epic : epicList.values()) {
            if (epic.subTaskElements.contains(mSubtaskID)) {
                return epic;
            }
        }
        return null;
    }

     ArrayList<Epic> getAllEpic() {

         ArrayList<Epic> epicArrayList = new ArrayList<Epic>();
         for (Epic epic: epicList.values()) {
             epicArrayList.add(epic);
         }
        return epicArrayList;
    }

     ArrayList<Task> getAllTasks(){

        ArrayList<Task> taskArrayList = new ArrayList<Task>();
        for (Task task: taskList.values()){
            taskArrayList.add(task);
        }
        return taskArrayList;
    }

     ArrayList<SubTask> getAllSubTasks() {

        ArrayList<SubTask> subTaskArrayList = new ArrayList<SubTask>();
         for (SubTask subTask: subTaskList.values()){
             subTaskArrayList.add(subTask);
         }
         return subTaskArrayList;
    }

    //Создание задач всех типов
    public void createEpic(Epic mEpic) {

        epicList.put(mEpic.getID(), mEpic);
    }

    public  void createTask(Task task) {

        taskList.put(task.getID(), task);
    }

    public  void createSubtask(int mEpicID, SubTask subTask) {

        Epic epic = getEpicByID(mEpicID);
        if (epic != null) {
            subTaskList.put(subTask.getID(), subTask);
            epic.subTaskElements.add(subTask.getID());
            epic.recountEpicState(subTaskList);

        } else {
            System.out.println("Указан неверный номер Эпика");
        }
    }


    // Удаление
    public  void deleteElement(int mID, String mType) {

        switch (mType) {
            case "EPIC":

                if (epicList.containsKey(mID)) {
                    epicList.remove(mID);
                } else {
                    System.out.println("Передан неверный номер Эпика");
                }
                break;
            case "TASK":

                if (taskList.containsKey(mID)) {
                    taskList.remove(mID);
                } else {
                    System.out.println("Передан неверный номер Задачи: " + mID);
                }
                break;
            case "SUB_TASK":

                if (subTaskList.containsKey(mID)) {
                    subTaskList.remove(mID);
                    Epic epic = getEpicBySubtaskID(mID);
                    if (epic != null) {
                        epic.subTaskElements.remove(Integer.valueOf(mID));
                        epic.recountEpicState(subTaskList);
                    }
                } else {
                    System.out.println("Передан неверный номер Подзадачи");
                }
                break;
            default:
                System.out.println("Передан неверный тип задачи");
        }
    }

    public  void deleteAllElements(String mType) {

        switch (mType) {
            case "EPIC":

                epicList.clear();
                break;
            case "TASK":

                taskList.clear();
                break;
            case "SUB_TASK":

                subTaskList.clear();  //Очищаем все сабтакси и обновляем статус у Эпика
                for (Epic epic : epicList.values()) {
                    epic.subTaskElements.clear();
                    epic.setState(TaskState.NEW);
                }
                break;
            default:
                System.out.println("Передан неверный тип задачи");
        }
    }

    public  int getElementID() {
        elementID++;
        return elementID;
    }

    //Обновление
    public  void updateEpic(int epicID, Epic epic) {

        epicList.put(epicID, epic);
    }

    public  void updateTask(int taskID, Task task) {

        taskList.put(taskID, task);
        task.updateState();
    }

    public void updateSubTask(int mSubTaskID, SubTask mSubTask) {

        subTaskList.put(mSubTaskID, mSubTask);
        Epic epic = getEpicBySubtaskID(mSubTaskID);
        mSubTask.updateState();
        epic.recountEpicState(subTaskList);
    }

    public  void printAllElements(String mType) {
        
        System.out.println("=====".repeat(5));
        switch (mType) {
            case "TASK":
                for (Task task: taskList.values()){
                    System.out.println(task);
                }
                break;
            case "EPIC":
                for (Epic epic: epicList.values()){
                    System.out.println(epic);
                }
                break;
            case "SUB_TASK":
                for (SubTask subTask: subTaskList.values()){
                    System.out.println(subTask);
                }
        }
        System.out.println("=====".repeat(5));
    }

}
