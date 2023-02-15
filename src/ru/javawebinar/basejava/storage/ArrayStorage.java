package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume resume) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("ERROR : storage is full");
        } else if (getIndex(resume.getUuid()) > -1) {
            System.out.println("ERROR : resume " + resume.getUuid() + " is already in the storage");
        } else {
            storage[size++] = resume;
        }
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
