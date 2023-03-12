package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage <K> implements Storage {

    @Override
    public abstract void clear();

    @Override
    public final Resume get(String uuid) {
        K searchKey = searchKey(uuid);
        if (isExist(searchKey)) {
            return doGet(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract Resume doGet(K searchKey);

    protected abstract boolean isExist(K searchKey);

    @Override
    public final void save(Resume resume) {
        K searchKey = searchKey(resume.getUuid());
        if (isExist(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        } else {
            doSave(resume);
        }
    }

    protected abstract void doSave(Resume resume);

    @Override
    public final void delete(String uuid) {
        K searchKey = searchKey(uuid);
        if (isExist(searchKey)) {
            doDelete(searchKey);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract void doDelete(K searchKey);

    @Override
    public final void update(Resume resume) {
        K searchKey = searchKey(resume.getUuid());
        if (isExist(searchKey)) {
            doUpdate(resume, searchKey);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    protected abstract void doUpdate(Resume resume, K searchKey);

    @Override
    public abstract Resume[] getAll();

    @Override
    public abstract int size();

    protected abstract K searchKey(String uuid);
}
