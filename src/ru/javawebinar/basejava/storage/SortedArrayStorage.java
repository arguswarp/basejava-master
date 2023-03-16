package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected void saveResume(Resume resume) {
        int index = -(getSearchKey(resume.getUuid())) - 1;
        if (size != 0) {
            System.arraycopy(storage, index, storage, index + 1, size - index);
        }
        storage[index] = resume;
        size++;
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
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey, Comparator.comparing(Resume::getUuid));
    }
}
