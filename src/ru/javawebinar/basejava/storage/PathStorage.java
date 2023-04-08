package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;

    private SerializationStrategy serializationStrategy;

    protected PathStorage(String dir, SerializationStrategy serializationStrategy) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(serializationStrategy, "serialization strategy must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory) || !Files.isReadable(directory)) {
            throw new IllegalArgumentException(dir + "is not directory or is not readable/writeable");
        }
        this.serializationStrategy = serializationStrategy;
    }

    protected PathStorage(String dir) {
        this(dir, new ObjectStreamSerialization());
    }

    public void setSerializationStrategy(SerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    @Override
    protected List<Resume> doCopyAll() {
        try {
            return Files.list(directory).map(this::doGet).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("directory read error", null);
        }
    }

    @Override
    protected void doSave(Resume resume) {
        Path path = getSearchKey(resume.getUuid());
        try {
            doWrite(resume, Files.newOutputStream(path, CREATE_NEW));
        } catch (IOException e) {
            throw new StorageException("path write error" + path.toAbsolutePath(), path.getFileName().toString(), e);
        }
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
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new StorageException("path delete error", path.getFileName().toString());
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Path.of(directory.toString(), uuid);
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("path delete error", null);
        }
    }

    @Override
    public int size() {
        try {
            return Files.list(directory).toList().size();
        } catch (IOException e) {
            throw new StorageException("directory read error", null);
        }
    }

    protected void doWrite(Resume resume, OutputStream outputStream) throws IOException {
        serializationStrategy.write(resume, outputStream);
    }

    protected Resume doRead(InputStream inputStream) throws IOException {
        return serializationStrategy.read(inputStream);
    }
}
