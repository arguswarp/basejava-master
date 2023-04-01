package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final File directory;

    private int size;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writeable");
        }
        this.directory = directory;
    }

    @Override
    protected List<Resume> doCopyAll() {
        File[] files = directory.listFiles();
        List<Resume> listCopy = new ArrayList<>();
        for (File f : files) {
            listCopy.add(doRead(f));
        }
        return listCopy;
    }

    @Override
    protected void doSave(Resume resume) {
        File file = getSearchKey(resume.getUuid());
        try {
            file.createNewFile();
            doWrite(file);
            size++;
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract void doWrite(File file) throws IOException;

    @Override
    protected Resume doGet(File file) {
        return doRead(file);
    }

    protected abstract Resume doRead(File file);

    @Override
    protected void doUpdate(Resume resume, File file) {
        doDelete(file);
        File updatedFile = getSearchKey(resume.getUuid());
        try {
            updatedFile.createNewFile();
            doWrite(updatedFile);
        } catch (IOException e) {
            throw new StorageException("IO error", updatedFile.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        file.delete();
        size--;
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        for (File f : files) {
            f.delete();
        }
    }

    @Override
    public int size() {
        return size;
    }
}
