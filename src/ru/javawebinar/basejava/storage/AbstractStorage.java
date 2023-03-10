package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public abstract void clear();

    @Override
    public final Resume get(String uuid) {
        Object searchKey = searchKey(uuid);
        if (isExist(searchKey)) {
            return doGet(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract <T> Resume doGet(T searchKey);

    protected abstract <T> boolean isExist(T searchKey);

    @Override
    public final void save(Resume resume) {
        Object searchKey = searchKey(resume.getUuid());
        if (isExist(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        } else {
            doSave(resume);
        }
    }

    protected abstract void doSave(Resume resume);

    @Override
    public final void delete(String uuid) {
        Object searchKey = searchKey(uuid);
        if (isExist(searchKey)) {
            doDelete(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract <T> void doDelete(T searchKey);

    @Override
    public final void update(Resume resume) {
        Object searchKey = searchKey(resume.getUuid());
        if (isExist(searchKey)) {
            doUpdate(resume, searchKey);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    protected abstract <T> void doUpdate(Resume resume, T searchKey);

    @Override
    public abstract Resume[] getAll();

    @Override
    public abstract int size();

    protected abstract <T> T searchKey(String uuid);
}
