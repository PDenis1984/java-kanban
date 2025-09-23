package ru.yandex.practicum.services;

import ru.yandex.practicum.models.*;
import ru.yandex.practicum.models.exceptioons.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final String FILE_HEADER = "id,type,name,status,description,epic\n";
    private final String fileName;

    //Создание

    public FileBackedTaskManager(String fileName) {

        super();
        this.fileName = fileName;
    }

    @Override
    public int createEpic(Epic mEpic) {

        int epicID = super.createEpic(mEpic);
        save();
        return epicID;
    }

    @Override
    public int createTask(Task mTask) {

        int taskID = super.createTask(mTask);
        save();
        return taskID;
    }

    @Override
    public int createSubTask(SubTask mSubTask) {

        int subTaskID = super.createSubTask(mSubTask);
        save();
        return subTaskID;
    }

    //Обновление
    @Override
    public void updateEpic(Epic mEpic) {

        super.updateEpic(mEpic);
        save();
    }

    @Override
    public void updateTask(Task mTask) {

        super.updateTask(mTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask mSubTask) {

        super.updateSubTask(mSubTask);
        save();
    }

    // Удаление
    @Override
    public void deleteAllElements(String mType) {

        super.deleteAllElements(mType);
        save();
    }

    @Override
    public void deleteElement(int mID, String mType) {

        super.deleteElement(mID, mType);
        save();
    }

    // Сохраняем текущие таски.
    private void save() throws ManagerSaveException {

        List<Task> allTasks = getAllTasks();
        allTasks.addAll(getAllEpic());
        allTasks.addAll(getAllSubTasks());

        try (BufferedWriter bfWriter = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {

            bfWriter.flush();
            bfWriter.append(FILE_HEADER);
            String writeString = "";
            for (Task task : allTasks) {

                writeString = taskToString(task) + "\n";
                bfWriter.append(writeString);
            }
        } catch (IOException ioEX) {
            System.out.println("Произошла ошибка при записи файла " + fileName);
            throw new ManagerSaveException("Произошла ошибка при записи файла " + fileName);
        }

    }


    private String taskToString(Task task) {

        StringBuilder sbTask = new StringBuilder();
        int epicID = 0;
        TaskType taskType = TaskType.TASK;
        sbTask.append(task.getID()).append(",");

        if (task instanceof SubTask) {

            epicID = ((SubTask) task).getEpicID();
            taskType = TaskType.SUBTASK;
        } else if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        }
        sbTask.append(String.valueOf(taskType)).append(',')
                .append(task.getName()).append(",")
                .append(String.valueOf(task.getState())).append(",")
                .append(task.getDescription()).append(",");

        if (taskType.equals(TaskType.SUBTASK)) {

            sbTask.append(epicID);
        }
        return sbTask.toString();
    }


    // Конструктор создает пустой менеджер. Здесь создаем менеджер и наполняем его задачами
    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager fBTManager = new FileBackedTaskManager(file.getName());
        try {
            BufferedReader bfReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            bfReader.readLine();
            int maxTaskNumber = 1;
            while (bfReader.ready()) {

                String[] fileRecord = bfReader.readLine().split(",");
                int elementID = Integer.parseInt(fileRecord[0]);

                if (elementID > maxTaskNumber) {

                    maxTaskNumber = elementID;
                }

                switch (fileRecord[1]) {

                    case "TASK":

                        Task task = new Task(fileRecord[2], fileRecord[4], TaskState.valueOf(fileRecord[3]));
                        task.setID(elementID);
                        fBTManager.taskList.put(elementID, task);
                        break;
                    case "EPIC":

                        Epic epic = new Epic(fileRecord[2], fileRecord[4]);
                        epic.setID(elementID);
                        fBTManager.epicList.put(elementID, epic);
                        break;
                    case "SUBTASK":

                        int parentEpicID = Integer.parseInt(fileRecord[5]);
                        SubTask subTask = new SubTask(fileRecord[2], fileRecord[4], parentEpicID, TaskState.valueOf(fileRecord[3]));
                        subTask.setID(elementID);
                        fBTManager.subTaskList.put(elementID, subTask);
                        break;
                }
            }

            for (SubTask subTask: fBTManager.getAllSubTasks()) {

                int subTaskID = subTask.getID();
                int epicID  =  subTask.getEpicID();
                fBTManager.getEpicByID(epicID).addSubTask(subTaskID);
            }
            for (Epic epic : fBTManager.getAllEpic()) {

                fBTManager.recountEpicState(epic);
            }

            fBTManager.elementID = maxTaskNumber + 1;

        } catch (FileNotFoundException findReadException) {

            System.out.println("Файл " + file.getName() + " не найден");
            throw  new ManagerSaveException("Файл " + file.getName() + " не найден");
        } catch (IOException readException) {

            System.out.println("Произошла ошибка при чтении файла " + file.getName());
            throw  new ManagerSaveException("Произошла ошибка при чтении файла " + file.getName());
        }
        return fBTManager;
    }
}
