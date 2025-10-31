package ru.yandex.practicum.helpers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationSerializeAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsWriter, Duration duration) throws IOException {
        if (duration == null) {
            jsWriter.nullValue(); // Обрабатываем null
        } else {
            jsWriter.value(String.valueOf(duration.toMinutes()));
        }
    }

    @Override
    public Duration read(JsonReader jsReader) throws IOException {
        if (jsReader.peek() == JsonToken.NULL) {
            jsReader.nextNull();
            return null;
        }
        return Duration.parse(jsReader.nextString());
    }
}