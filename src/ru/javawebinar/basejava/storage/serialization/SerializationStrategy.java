package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializationStrategy {

    void write(Resume resume, OutputStream outputStream) throws IOException;

    Resume read(InputStream inputStream) throws IOException;
}
