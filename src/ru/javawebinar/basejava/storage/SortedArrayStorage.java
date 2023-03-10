package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected <T> T searchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        Integer result = Arrays.binarySearch(storage, 0, size, searchKey);
        return (T) result;
    }

    @Override
    protected <T> void doDelete(T searchKey) {
        int index = (int) searchKey;
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index + 1, storage, index, numMoved);
            size--;
        }
    }

    @Override
    protected void saveResume(Resume resume) {
        int index = -(int) (searchKey(resume.getUuid())) - 1;
        if (size != 0) {
            System.arraycopy(storage, index, storage, index + 1, size - index);
        }
        storage[index] = resume;
        size++;
    }
}
