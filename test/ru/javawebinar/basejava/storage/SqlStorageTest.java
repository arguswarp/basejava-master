package ru.javawebinar.basejava.storage;

import static org.junit.jupiter.api.Assertions.*;

class SqlStorageTest extends AbstractStorageTest {

    protected SqlStorageTest() {
        super(new SqlStorage(DB_URL,DB_USER,DB_PASSWORD));
    }
}