package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected <T> T searchKey(String uuid) {
        Integer index = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                index = i;
                break;
            }
        }
        return (T) index;
    }

    @Override
    protected <T> void doDelete(T searchKey) {
        int index = (int) searchKey;
        storage[index] = storage[size - 1];
        size--;
    }

    @Override
    protected void saveResume(Resume resume) {
        storage[size] = resume;
        size++;
    }
}
