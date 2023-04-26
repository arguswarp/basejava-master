package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Disabled;

class SqlStorageTest extends AbstractStorageTest {

    protected SqlStorageTest() {
        super(new SqlStorage(DB_URL, DB_USER, DB_PASSWORD));
    }

    @Override
    @Disabled
    public void saveExist() {
    }

    @Override
    @Disabled
    public void deleteNotExist() {
    }

    @Override
    @Disabled
    public void updateNotExist() {
    }
}