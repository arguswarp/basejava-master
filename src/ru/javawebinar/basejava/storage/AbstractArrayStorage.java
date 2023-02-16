package ru.javawebinar.basejava.storage;

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
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            return storage[index];
        } else {
            System.out.println("ERROR : no such uuid " + uuid + " in the storage");
            return null;
        }
    }

    @Override
    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (size >= STORAGE_LIMIT) {
            System.out.println("ERROR : storage is full");
        } else if (index > -1) {
            System.out.println("ERROR : resume " + resume.getUuid() + " is already in the storage");
        } else {
            saveResume(resume, index);
            size++;
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            deleteByIndex(index);
            size--;
        } else {
            System.out.println("ERROR : no such uuid " + uuid + " in the storage");
        }
    }

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index > -1) {
            storage[index] = resume;
        } else {
            System.out.println("ERROR : resume " + resume.getUuid() + " is not present in the storage");
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
