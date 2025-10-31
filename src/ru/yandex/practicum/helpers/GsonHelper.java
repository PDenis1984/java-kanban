package ru.yandex.practicum.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class GsonHelper {

    class TaskList extends TypeToken<List<Task>> {
    }

    public static String serializeTask(Task mTask) {

        try {
            Gson gson = buildGson();
            String result = gson.toJson(mTask);
            return result;

        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    public static String serializeTasks(List<? extends  Task> mTasks) {

        try {
            Gson gson = buildGson();
            String response = gson.toJson(mTasks);
            return response;
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
            String response = gson.toJson(taskString);
            return gson.fromJson(taskString, Task.class);
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


    public static SubTask deserializeSubTask (String mSubTaskString) {

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
