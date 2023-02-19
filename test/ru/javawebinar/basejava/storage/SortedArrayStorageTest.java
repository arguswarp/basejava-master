package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.*;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

}