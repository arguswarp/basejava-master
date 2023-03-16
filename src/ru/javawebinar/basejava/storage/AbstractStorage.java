package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<K> implements Storage {

    @Override
    public final void save(Resume resume) {
        getExistingSearchKey(resume.getUuid());
        doSave(resume);
    }

    @Override
    public final Resume get(String uuid) {
        return doGet(getNotExistingSearchKey(uuid));
    }

    @Override
    public final void update(Resume resume) {
        doUpdate(resume, getNotExistingSearchKey(resume.getUuid()));
    }

    @Override
    public final void delete(String uuid) {
        doDelete(getNotExistingSearchKey(uuid));
    }

    private void getExistingSearchKey(String uuid) {
        K searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
    }

    private K getNotExistingSearchKey(String uuid) {
        K searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            return searchKey;
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        return sortResumes(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
    }

    protected abstract List<Resume> sortResumes(Comparator<Resume> resumeComparator);

    protected abstract void doSave(Resume resume);

    protected abstract Resume doGet(K searchKey);

    protected abstract void doUpdate(Resume resume, K searchKey);

    protected abstract void doDelete(K searchKey);

    protected abstract boolean isExist(K searchKey);

    protected abstract K getSearchKey(String uuid);
}
