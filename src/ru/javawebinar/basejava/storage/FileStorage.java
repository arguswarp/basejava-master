package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serialization.ObjectStreamSerialization;
import ru.javawebinar.basejava.storage.serialization.SerializationStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private final File directory;

    private SerializationStrategy serializationStrategy;

    protected FileStorage(File directory, ObjectStreamSerialization serializationStrategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writeable");
        }
        this.directory = directory;
        this.serializationStrategy = serializationStrategy;
    }

    protected FileStorage(File directory) {
        this(directory, new ObjectStreamSerialization());
    }

    public void setSerializationStrategy(SerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> listCopy = new ArrayList<>();
        for (File f : getFiles()) {
            listCopy.add(doGet(f));
        }
        return listCopy;
    }

    @Override
    protected void doSave(Resume resume) {
        File file = getSearchKey(resume.getUuid());
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("file write error" + file.getAbsolutePath(), file.getName(), e);
        }
        doUpdate(resume, file);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("file read error", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(Resume resume, File file) {
        try {
            doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("file update error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("file delete error", file.getName());
        }
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
        for (File f : getFiles()) {
            doDelete(f);
        }
    }

    @Override
    public int size() {
        return getFiles().length;
    }

    private File[] getFiles() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("directory read error", null);
        }
        return files;
    }

    protected void doWrite(Resume resume, OutputStream outputStream) throws IOException {
        serializationStrategy.write(resume, outputStream);
    }

    protected Resume doRead(InputStream inputStream) throws IOException {
        return serializationStrategy.read(inputStream);
    }
}
