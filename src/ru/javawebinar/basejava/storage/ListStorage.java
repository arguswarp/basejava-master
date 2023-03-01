package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListStorage extends AbstractStorage {
    private static final List<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
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
    protected int getIndex(String uuid) {
        for (Resume resume : listStorage) {
            if (Objects.equals(resume.getUuid(), uuid)) {
                return listStorage.indexOf(resume);
            }
        }
        return -1;
    }

    @Override
    protected void deleteByIndex(int index) {
        listStorage.remove(index);
    }

    @Override
    protected void saveResume(Resume resume, int index) {
        listStorage.add(resume);
    }

    @Override
    protected Resume getResume(int index) {
        return listStorage.get(index);
    }

    @Override
    protected void updateResume(Resume resume, int index) {
        listStorage.set(index, resume);
    }

    @Override
    protected void increaseSize() {

    }

    @Override
    protected void decreaseSize() {

    }

    @Override
    protected void eraseLastElement() {

    }
}
