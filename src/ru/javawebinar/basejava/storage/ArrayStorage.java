package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer searchKey(String uuid) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void doDelete(Integer searchKey) {
        storage[searchKey] = storage[size - 1];
        size--;
    }

    @Override
    protected void saveResume(Resume resume) {
        storage[size] = resume;
        size++;
    }
}
