package ru.javawebinar.basejava.storage.serialization;

import java.io.IOException;

public interface DataConsumer<T> {
    void accept(T t) throws IOException;
}