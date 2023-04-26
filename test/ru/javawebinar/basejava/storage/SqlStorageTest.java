package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Disabled;
import ru.javawebinar.basejava.Config;

class SqlStorageTest extends AbstractStorageTest {

    protected SqlStorageTest() {
        super(Config.get().getStorage());
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