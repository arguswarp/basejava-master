package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (size >= STORAGE_LIMIT) {
            System.out.println("ERROR : storage is full");
        } else if (index > -1) {
            System.out.println("ERROR : resume " + resume.getUuid() + " is already in the storage");
        } else {
            index = -(index)-1;
            if (size != 0) {
                System.arraycopy(storage, index, storage, index + 1, size - index);
            }
            storage[index] = resume;
            size++;
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
