package ru.yandex.practicum.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.models.Task;

import java.time.LocalTime;
import java.util.List;

public class GsonHelper {

    class SubtitleListTypeToken extends TypeToken<List<Task>> {
    }

    public static String serializeTask(Task mTask) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new DateTimeSerializeAdapter());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(mTask);
    }

    public static  String serializeTasks(List<Task> mTasks) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new DateTimeSerializeAdapter());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(mTasks);
    }
}
