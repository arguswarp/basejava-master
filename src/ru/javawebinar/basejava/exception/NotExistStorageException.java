package ru.javawebinar.basejava.exception;

public class NotExistStorageException extends StorageException {
    public NotExistStorageException(String uuid) {
        super("No such resume " + uuid + " in the storage", uuid);
    }
}
