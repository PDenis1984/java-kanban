package ru.yandex.practicum;

import ru.yandex.practicum.models.*;


public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        int newValue = 1;
        fillTasks();
    }

    public static  void fillTasks() {

        Epic epic1 = new Epic( TaskManager.getElementID(), "Подготовка к зачету", "Выучить необходимые параграфы, решить задачи, написать шпоаргалки");
        TaskManager.createEpic(epic1);
        SubTask subtask1 = new SubTask(TaskManager.getElementID(), "Изучение параграф1", "Страницы с 5 по 12", epic1.getID());
        TaskManager.createSubtask(epic1.getID(), subtask1);
        SubTask subTask2 = new SubTask(TaskManager.getElementID(), "Решить задачу", "Задачи, номер 2, 5", epic1.getID());
        Epic epic2 = new Epic(TaskManager.getElementID(), "Приготовить обед", "Комплекный обед");
        TaskManager.createEpic(epic2);
        SubTask subTask21 = new SubTask(TaskManager.getElementID(), "Борщ", "Мясо,  свекла, овощи", epic2.getID());
        TaskManager.createSubtask(epic2.getID(), subTask21);

        Task task1 = new Task(TaskManager.getElementID(), "Сходить в магазин", "За хлебом");
        TaskManager.createTask(task1);
        Task task2 = new Task(TaskManager.getElementID(), "Починить дверь", "Вставить глазок и замок");
        TaskManager.createTask(task2);
        TaskManager.printAllElements("TASK");
        TaskManager.printAllElements("EPIC");
        TaskManager.printAllElements("SUB_TASK");

        epic1.setDescription("Изучить необходимые параграфы");
        TaskManager.updateEpic(1, epic1);
        epic2.setDescription("Бизнес-ланч");
        TaskManager.updateEpic(2, epic2);
        subTask2.setDescription("Задача 10");
        TaskManager.updateSubTask(2, subTask2);


        TaskManager.printAllElements("TASK");
        TaskManager.printAllElements("EPIC");
        TaskManager.printAllElements("SUB_TASK");

    }


}

