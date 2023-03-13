package ru.javawebinar.basejava.storage;

import static org.junit.jupiter.api.Assertions.*;

public class MapStorageTest extends AbstractStorageTest {
    public MapStorageTest() {
        super(new MapStorage());
    }
}