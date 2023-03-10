package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private static final List<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    protected <T> Resume doGet(T searchKey) {
        return listStorage.get((int) searchKey);
    }

    @Override
    protected <T> boolean isExist(T searchKey) {
        int index = (int) searchKey;
        return index > -1;
    }

    @Override
    protected void doSave(Resume resume) {
        listStorage.add(resume);
    }

    @Override
    protected <T> void doDelete(T searchKey) {
        int index = (int) searchKey;
        listStorage.remove(index);
    }

    @Override
    protected <T> void doUpdate(Resume resume, T searchKey) {
        int index = (int) searchKey;
        listStorage.set(index, resume);
    }

    @Override
    public Resume[] getAll() {
        return listStorage.toArray(new Resume[size()]);
    }

    @Override
    public int size() {
        return listStorage.size();
    }

    @Override
    protected <T> T searchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return (T) Integer.valueOf(listStorage.indexOf(searchKey));
    }

}
