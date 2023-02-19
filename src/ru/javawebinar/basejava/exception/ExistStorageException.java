package ru.javawebinar.basejava.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super("No such resume " + uuid + " in the storage", uuid);
    }
}
