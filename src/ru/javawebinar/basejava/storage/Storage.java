package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public interface Storage {

    void clear();

    public void save(Resume resume);

    public Resume get(String uuid);

    public void delete(String uuid);

    public void update(Resume resume);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll();

    public int size();
}
