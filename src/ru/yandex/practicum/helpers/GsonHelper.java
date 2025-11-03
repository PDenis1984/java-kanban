package ru.yandex.practicum.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class GsonHelper {

    public static String serializeTask(Task mTask) {

        try {

            Gson gson = buildGson();
            return gson.toJson(mTask);

        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    public static String serializeTasks(List<? extends  Task> mTasks) {

        try {

            Gson gson = buildGson();
            return  gson.toJson(mTasks);
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    private static Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new DateTimeSerializeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationSerializeAdapter());
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();
    }

    public static Task deserializeTask(String taskString) {

        try {
            Gson gson = buildGson();
            return gson.fromJson(taskString, Task.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static List<Task> deserializeTasks(String tasksString) {

        try {
            Gson gson = buildGson();
            Type listType = new TypeToken<List<Task>>(){}.getType();
            return gson.fromJson(tasksString, listType);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static Epic deserializeEpic(String epicString) {

        try {
            Gson gson = buildGson();
            String response = gson.toJson(epicString);
            return gson.fromJson(epicString, Epic.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }


    public static SubTask deserializeSubTask(String mSubTaskString) {

        try {
            Gson gson = buildGson();
            String response = gson.toJson(mSubTaskString);
            return gson.fromJson(mSubTaskString, SubTask.class);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

}
