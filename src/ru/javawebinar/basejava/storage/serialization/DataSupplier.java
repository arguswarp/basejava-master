package ru.javawebinar.basejava.storage.serialization;

import java.io.IOException;

public interface DataSupplier <T>{
    T get() throws IOException;
}
