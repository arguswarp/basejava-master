package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected static final int STORAGE_LIMIT = 10_000;

    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            return storage[index];
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public final void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        } else if (index > -1) {
            throw new ExistStorageException(resume.getUuid());
        } else {
            saveResume(resume, index);
            size++;
        }
    }

    @Override
    public final void delete(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            deleteByIndex(index);
            storage[size] = null;
            size--;
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public final void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index > -1) {
            storage[index] = resume;
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void deleteByIndex(int index);

    protected abstract void saveResume(Resume resume, int index);
}
