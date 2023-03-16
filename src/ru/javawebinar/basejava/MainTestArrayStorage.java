package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SortedArrayStorage;
import ru.javawebinar.basejava.storage.Storage;

/**
 * Test for your com.urise.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    private static final Storage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {

        Resume r1 = new Resume("uuid3");
        Resume r2 = new Resume("uuid1");
        Resume r3 = new Resume("uuid2");
        Resume r4 = new Resume("uuid4");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r4);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());
        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));
        printAll();

        Resume r2update = new Resume("uuid2");
        ARRAY_STORAGE.update(r2update);
        printAll();

        Resume r2update2 = new Resume("uuid36");
        ARRAY_STORAGE.update(r2update2);
        printAll();

        ARRAY_STORAGE.delete(r1.getUuid());
        ARRAY_STORAGE.delete(r2.getUuid());
        printAll();
        System.out.println("Size: " + ARRAY_STORAGE.size());
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());

        for (int i = 9_999; i >= 0; i--) {
            Resume r = new Resume("uuid" + i);
            ARRAY_STORAGE.save(r);
        }

        System.out.println("Size: " + ARRAY_STORAGE.size());

        Resume r10000 = new Resume("uuid9999");
        ARRAY_STORAGE.save(r10000);
        Resume rOverflow = new Resume("uuid10000");
        ARRAY_STORAGE.save(rOverflow);
        System.out.println("Size: " + ARRAY_STORAGE.size());

        ARRAY_STORAGE.clear();
        printAll();
        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}
