package ru.yandex.practicum.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.models.Task;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class GsonHelper {

    class TaskList extends TypeToken<List<Task>> {
    }

    public static String serializeTask(Task mTask) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new DateTimeSerializeAdapter());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(mTask);
    }

    public static  String serializeTasks(List<Task> mTasks) {


        return mTasks.stream()
                .map(Task::toString)
                .filter(str -> !str.isEmpty())
                .collect(Collectors.joining("\n"));
    }

    public static Task deserializeTask(String taskString) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new DateTimeSerializeAdapter());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(taskString, Task.class);
    }

}
