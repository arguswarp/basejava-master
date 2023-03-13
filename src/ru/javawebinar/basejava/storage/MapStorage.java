package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage<String> {
    private static final Map<String, Resume> mapStorage = new HashMap<>();

    @Override
    public Resume[] getAll() {
        Resume[] result = new Resume[size()];
        int i = 0;
        for (Map.Entry<String, Resume> entrySet : mapStorage.entrySet()) {
            result[i] = entrySet.getValue();
            i++;
        }
        return result;
    }

    @Override
    public int size() {
        return mapStorage.size();
    }

    @Override
    public void clear() {
        mapStorage.clear();
    }

    @Override
    protected void doSave(Resume resume) {
        mapStorage.put(searchKey(resume.getUuid()), resume);
    }

    @Override
    protected Resume doGet(String searchKey) {
        return mapStorage.get(searchKey);
    }

    @Override
    protected void doUpdate(Resume resume, String searchKey) {
        mapStorage.replace(searchKey, resume);
    }

    @Override
    protected void doDelete(String searchKey) {
        mapStorage.remove(searchKey);
    }

    @Override
    protected boolean isExist(String searchKey) {
        return mapStorage.containsKey(searchKey);
    }

    @Override
    protected String searchKey(String uuid) {
        return uuid;
    }
}
