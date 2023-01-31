import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    private int size = 0;

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        //  System.out.println("No such uuid in the Storage");
        return null;
    }

    void delete(String uuid) {
        if (get(uuid) != null) {
            int index = -1;
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    storage[i] = null;
                    index = i;
                    size--;
                }
            }
            for (int i = 0; i < size; i++) {
                if (i >= index) {
                    storage[i] = storage[i + 1];
                }
            }
            storage[size + 1] = null;
        } else {
            System.out.println("No such uuid in the Storage");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        if (size == 0) {
            System.out.println("Storage is empty");
        }
        return Arrays.copyOfRange(storage, 0, size);
    }

    int size() {

        return size;
    }

}
