package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;

class SqlStorageTest extends AbstractStorageTest {

    protected SqlStorageTest() {
        super(Config.get().getStorage());
    }
}