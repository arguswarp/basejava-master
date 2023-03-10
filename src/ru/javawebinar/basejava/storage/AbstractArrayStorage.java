package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected static final int STORAGE_LIMIT = 10_000;

    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected final void doSave(Resume resume) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        } else {
            saveResume(resume);
        }
    }

    protected abstract void saveResume(Resume resume);

    @Override
    protected <T> Resume doGet(T searchKey) {
        int index = (int) searchKey;
        return storage[index];
    }

    @Override
    protected <T> void doUpdate(Resume resume, T searchKey) {
        int index = (int) searchKey;
        storage[index] = resume;
    }

    @Override
    protected <T> boolean isExist(T searchKey) {
        int index = (int) searchKey;
        return index > -1;
    }
}
