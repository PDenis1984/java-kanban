package ru.yandex.practicum.intf;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.List;

public interface TaskManagerIntf {
    // Набор для получения задач
    Task getTaskByID(int mID);

    Epic getEpicByID(int mID);

    Epic getEpicBySubTaskID(int mSubtaskID);

    SubTask getSubTaskByID(int mSubTaskID);

    List<Epic> getAllEpic();

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<SubTask> getAllSubTaskByEpicID(int mEpicID);

    //Создание задач всех типов
    int createEpic(Epic mEpic);

    int createTask(Task mTask);

    int createSubTask(SubTask mSubTask);

    // Удаление
    void deleteElement(int mID, String mType);

    void deleteAllElements(String mType);

    //Обновление
    void updateEpic(Epic mEpic);

    void updateTask(Task mTask);

    void updateSubTask(SubTask mSubTask);

    List<Task> getHistory();

    void printAllElements(String taskType);
}
