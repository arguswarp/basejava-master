package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File>{

    private final File directory;

    protected AbstractFileStorage (File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new  IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new  IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writeable");
        }
        this.directory = directory;
    }
    @Override
    protected List<Resume> doCopyAll() {
        return null;
    }

    @Override
    protected void doSave(Resume resume) {
        File file = new File(directory.getAbsolutePath());
        try {
            file.createNewFile();
            doWrite(file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract void doWrite(File file) throws IOException;

    @Override
    protected Resume doGet(File file) {
        return null;
    }

    @Override
    protected void doUpdate(Resume resume, File file) {

    }

    @Override
    protected void doDelete(File file) {

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

    }

    @Override
    public int size() {
        return 0;
    }
}
