package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private static final List<Resume> listStorage = new ArrayList<>();


    @Override
    protected void doSave(Resume resume) {
        listStorage.add(resume);
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return listStorage.get((int) searchKey);
    }

    @Override
    protected void doUpdate(Resume resume, Integer searchKey) {
        int index = (int) searchKey;
        listStorage.set(index, resume);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        int index = (int) searchKey;
        listStorage.remove(index);
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
    public void clear() {
        listStorage.clear();
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        int index = (int) searchKey;
        return index > -1;
    }

    @Override
    protected Integer searchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Integer.valueOf(listStorage.indexOf(searchKey));
    }
}
