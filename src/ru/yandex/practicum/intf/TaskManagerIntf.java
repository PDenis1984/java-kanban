package ru.yandex.practicum.intf;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.ArrayList;

public interface TaskManagerIntf {
    // Набор для получения задач
    Task getTaskByID(int mID);

    Epic getEpicByID(int mID);

    Epic getEpicBySubTaskID(int mSubtaskID);

    SubTask getSubTaskByID(int mSubTaskID);

    ArrayList<Epic> getAllEpic();

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<SubTask> getAllSubTaskByEpicID(int mEpicID);

    //Создание задач всех типов
    int createEpic(Epic mEpic);

    int createTask(Task mTask);

    int createSubtask(SubTask mSubTask);

    // Удаление
    void deleteElement(int mID, String mType);

    void deleteAllElements(String mType);

    //Обновление
    void updateEpic(Epic mEpic);

    void updateTask(Task mTask);

    void updateSubTask(SubTask mSubTask);
}
