package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<K> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private static final Comparator<Resume> COMPARATOR_RESUME = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    @Override
    public final void save(Resume resume) {
        LOG.info("Save " + resume);
        getExistingSearchKey(resume.getUuid());
        doSave(resume);
    }

    @Override
    public final Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return doGet(getNotExistingSearchKey(uuid));
    }

    @Override
    public final void update(Resume resume) {
        LOG.info("Update " + resume);
        doUpdate(resume, getNotExistingSearchKey(resume.getUuid()));
    }

    @Override
    public final void delete(String uuid) {
        LOG.info("Delete " + uuid);
        doDelete(getNotExistingSearchKey(uuid));
    }

    private void getExistingSearchKey(String uuid) {

        K searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
    }

    private K getNotExistingSearchKey(String uuid) {
        K searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            return searchKey;
        } else {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted ");
        List<Resume> resumes = new ArrayList<>(doCopyAll());
        resumes.sort(COMPARATOR_RESUME);
        return resumes;
    }

    protected abstract List<Resume> doCopyAll();

    protected abstract void doSave(Resume resume);

    protected abstract Resume doGet(K searchKey);

    protected abstract void doUpdate(Resume resume, K searchKey);

    protected abstract void doDelete(K searchKey);

    protected abstract boolean isExist(K searchKey);

    protected abstract K getSearchKey(String uuid);
}
