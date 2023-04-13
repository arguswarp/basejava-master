package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.util.JsonParser;

import java.io.*;

public class JsonStreamSerializer implements StreamSerializer {

    @Override
    public void write(Resume resume, OutputStream outputStream) throws IOException {
        try (Writer writer = new OutputStreamWriter(outputStream)) {
            JsonParser.write(resume, writer);
        }
    }

    @Override
    public Resume read(InputStream inputStream) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream)) {
            return JsonParser.read(reader, Resume.class);
        }
    }
}
