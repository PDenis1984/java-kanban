package ru.yandex.practicum.intf;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.exceptioons.TaskOverlapException;

import java.util.List;
import java.util.Optional;

public interface TaskManagerIntf {
    // Набор для получения задач
    Optional<Task>  getTaskByID(int mID);

    Optional<Epic> getEpicByID(int mID);

    Epic getEpicBySubTaskID(int mSubtaskID);

    Optional<SubTask> getSubTaskByID(int mSubTaskID);

    List<Epic> getAllEpic();

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<SubTask> getAllSubTaskByEpicID(int mEpicID);

    //Создание задач всех типов
    int createEpic(Epic mEpic);

    Optional<Integer> createTask(Task mTask) throws TaskOverlapException;

    Optional<Integer> createSubTask(SubTask mSubTask) throws TaskOverlapException;

    // Удаление
    void deleteElement(int mID, String mType);

    void deleteAllElements(String mType);

    //Обновление
    boolean updateEpic(Epic mEpic);

    boolean updateTask(Task mTask) throws TaskOverlapException;

    boolean updateSubTask(SubTask mSubTask) throws TaskOverlapException;

    List<Task> getHistory();

    void printAllElements(String taskType);

    List<Task> getPrioritizeTasks();
}
