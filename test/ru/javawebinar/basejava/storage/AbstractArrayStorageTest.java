package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void get() {
        assertEquals(new Resume(UUID_1), storage.get(UUID_1));
        assertEquals(new Resume(UUID_2), storage.get(UUID_2));
    }

    @Test
    public void getNotExist() {
        String dummy = "dummy";
        NotExistStorageException exception = Assertions.assertThrows(NotExistStorageException.class, () -> storage.get(dummy));
        Assertions.assertEquals("Resume " + dummy + " is already in the storage", exception.getMessage());
    }

    @Test
    public void save() {
        storage.save(new Resume("uuid4"));
        assertEquals(storage.get("uuid4"), new Resume("uuid4"));
        assertEquals(4, storage.size());
    }

    @Test
    public void delete() {
        storage.delete("uuid2");
        assertEquals(2, storage.size());
    }

    @Test
    public void update() {
        storage.update(new Resume(UUID_1));
        assertEquals(new Resume(UUID_1), storage.get(UUID_1));
    }

    @Test
    public void getAll() {
        Resume[] resumes = {new Resume(UUID_1), new Resume(UUID_2), new Resume(UUID_3)};
        assertArrayEquals(resumes,storage.getAll());
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }
}