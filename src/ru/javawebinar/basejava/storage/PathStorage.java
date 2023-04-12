package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serialization.ObjectStreamSerializer;
import ru.javawebinar.basejava.storage.serialization.StreamSerializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.WRITE;

public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;

    private StreamSerializer streamSerializer;

    protected PathStorage(String dir, StreamSerializer streamSerializer) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(streamSerializer, "serialization strategy must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory) || !Files.isReadable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not readable/writeable");
        }
        this.streamSerializer = streamSerializer;
    }

    protected PathStorage(String dir) {
        this(dir, new ObjectStreamSerializer());
    }

    public void setSerializationStrategy(StreamSerializer streamSerializer) {
        this.streamSerializer = streamSerializer;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return getFilesList().map(this::doGet).toList();
    }

    @Override
    protected void doSave(Resume resume) {
        Path path = getSearchKey(resume.getUuid());
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("path write error" + path.toAbsolutePath(), path.getFileName().toString(), e);
        }
        doUpdate(resume, path);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("path read error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Resume resume, Path path) {
        try {
            doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path, WRITE)));
        } catch (IOException e) {
            throw new StorageException("path write error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("path delete error", path.getFileName().toString());
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.isRegularFile(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    public void clear() {
        getFilesList().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getFilesList().count();
    }

    private Stream<Path> getFilesList() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("directory read error", e);
        }
    }

    protected void doWrite(Resume resume, OutputStream outputStream) throws IOException {
        streamSerializer.write(resume, outputStream);
    }

    protected Resume doRead(InputStream inputStream) throws IOException {
        return streamSerializer.read(inputStream);
    }
}
