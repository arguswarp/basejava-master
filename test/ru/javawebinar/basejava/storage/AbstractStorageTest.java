package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected final Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_SAVED = "saved";
    private static final String UUID_NOT_EXIST = "dummy";
    private static final String FULL_NAME_1 = "Ivanov Ivan Ivanovich";
    private static final String FULL_NAME_2 = "John Doe";
    private static final String FULL_NAME_3 = "Joseph Joestar";
    private static final String FULL_NAME_SAVED = "Gandalf the Grey";
    private final Resume RESUME_1 = new Resume(UUID_1, FULL_NAME_1);
    private final Resume RESUME_2 = new Resume(UUID_2, FULL_NAME_2);
    private final Resume RESUME_3 = new Resume(UUID_3, FULL_NAME_3);
    private final Resume RESUME_SAVED = new Resume(UUID_SAVED, FULL_NAME_SAVED);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        Resume[] emptyStorage = new Resume[0];
        assertArrayEquals(emptyStorage, storage.getAllSorted().toArray());
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    private void assertGet(Resume expectedResume) {
        String uuid = expectedResume.getUuid();
        assertEquals(expectedResume, storage.get(uuid));
    }

    @Test
    public void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_NOT_EXIST));
    }

    @Test
    public void save() {
        storage.save(RESUME_SAVED);
        assertGet(RESUME_SAVED);
        assertSize(4);
    }

    @Test
    public void saveExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(new Resume(UUID_1, FULL_NAME_1)));
    }

    @Test
    @Disabled
    public void saveOverflow() {
    }

    @Test
    public void delete() {
        storage.delete(UUID_2);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_2));
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete(UUID_NOT_EXIST));
    }

    @Test
    public void update() {
        storage.update(RESUME_3);
        assertSame(RESUME_3, storage.get(UUID_3));
    }

    @Test
    public void updateNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume(UUID_NOT_EXIST)));
    }

    @Test
    public void getAllSorted() {
        List<Resume> resumes = new ArrayList<>();

        resumes.add(new Resume(UUID_1, FULL_NAME_1));
        resumes.add(new Resume(UUID_2, FULL_NAME_2));
        resumes.add(new Resume(UUID_3, FULL_NAME_3));

        List<Resume> resumesSorted = storage.getAllSorted();

        assertTrue(isListsEquals(resumes, resumesSorted));

        resumes.remove(0);
        resumes.add(new Resume(UUID_1, FULL_NAME_1));

        assertFalse(isListsEquals(resumes, resumesSorted));
    }

    private boolean isListsEquals(List<Resume> listExpected, List<Resume> listActual) {
        for (int i = 0; i < listActual.size(); i++) {
            if (!Objects.equals(listExpected.get(i), listActual.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int expected) {
        assertEquals(expected, storage.size());
    }
}
