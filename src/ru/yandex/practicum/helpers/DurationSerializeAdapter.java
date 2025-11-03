package ru.yandex.practicum.helpers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeParseException;

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
        String durationStr = jsReader.nextString();

        try {
            long minutes = Long.parseLong(durationStr);
            return Duration.ofMinutes(minutes);
        } catch (NumberFormatException e) {
            throw new DateTimeParseException(
                    "Duration must be a number representing minutes",
                    durationStr, 0);
        }
    }
}