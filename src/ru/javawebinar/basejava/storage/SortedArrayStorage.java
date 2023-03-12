package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer searchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        int numMoved = size - searchKey - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, searchKey + 1, storage, searchKey, numMoved);
            size--;
        }
    }

    @Override
    protected void saveResume(Resume resume) {
        int index = -(searchKey(resume.getUuid())) - 1;
        if (size != 0) {
            System.arraycopy(storage, index, storage, index + 1, size - index);
        }
        storage[index] = resume;
        size++;
    }
}
