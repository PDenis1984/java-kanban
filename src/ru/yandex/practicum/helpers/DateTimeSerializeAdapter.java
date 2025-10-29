package ru.yandex.practicum.helpers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeSerializeAdapter extends TypeAdapter<LocalTime> {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    @Override
    public void write(JsonWriter jsWriter, LocalTime lDateTime) throws IOException {
        jsWriter.value(lDateTime.format(timeFormatter));
    }

    @Override
    public LocalTime read(JsonReader jsReader) throws IOException {

        return LocalTime.parse(jsReader.nextString(), timeFormatter);
    }

}
