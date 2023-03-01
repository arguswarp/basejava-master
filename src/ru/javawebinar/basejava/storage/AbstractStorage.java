package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage{
    protected static final int STORAGE_LIMIT = 10_000;

    protected int size = 0;

    @Override
    public abstract void clear();

    @Override
    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            return getResume(index);
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
            increaseSize();
        }
    }

    @Override
    public final void delete(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            deleteByIndex(index);
            eraseLastElement();
            decreaseSize();
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public final void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index > -1) {
            updateResume(resume, index);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    public abstract Resume[] getAll();

    @Override
    public abstract int size();

    protected abstract int getIndex(String uuid);

    protected abstract void deleteByIndex(int index);

    protected abstract void saveResume(Resume resume, int index);

    protected abstract Resume getResume(int index);
    protected abstract void updateResume(Resume resume, int index);

    protected abstract void increaseSize();
    protected abstract void decreaseSize();
    protected abstract void eraseLastElement();

}
