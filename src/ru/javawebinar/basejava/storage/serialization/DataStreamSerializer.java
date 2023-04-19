package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DataStreamSerializer implements StreamSerializer {

    private static final String NULL_HOLDER = "THIS_IS_NULL_HOLDER";

    @Override
    public void write(Resume resume, OutputStream outputStream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            writeWithException(resume.getContacts().entrySet(), dataOutputStream, contact -> {
                dataOutputStream.writeUTF(contact.getKey().name());
                dataOutputStream.writeUTF(contact.getValue());
            });
            writeWithException(resume.getSections().entrySet(), dataOutputStream, section -> {
                dataOutputStream.writeUTF(section.getKey().name());
                writeSection(section.getKey(), section.getValue(), dataOutputStream);
            });
        }
    }

    private void writeSection(SectionType sectionType, AbstractSection section, DataOutputStream dataOutputStream) throws IOException {
        switch (sectionType) {
            case OBJECTIVE, PERSONAL -> dataOutputStream.writeUTF(((TextSection) section).getContent());
            case ACHIEVEMENT, QUALIFICATIONS ->
                    writeWithException(((ListSection) section).getItems(), dataOutputStream, dataOutputStream::writeUTF);
            case EXPERIENCE, EDUCATION ->
                    writeWithException(((CompanySection) section).getCompanies(), dataOutputStream, company -> {
                        dataOutputStream.writeUTF(company.getHomepage().getName());
                        writeNull(company.getHomepage().getUrl(), dataOutputStream);
                        writeWithException(company.getPeriods(), dataOutputStream, period -> {
                            dataOutputStream.writeUTF(period.getStartDate().toString());
                            dataOutputStream.writeUTF(period.getEndDate().toString());
                            dataOutputStream.writeUTF(period.getTitle());
                            writeNull(period.getDescription(), dataOutputStream);
                        });
                    });
        }
    }

    private static void writeNull(String s, DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF(s != null ? s : NULL_HOLDER);
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dataOutputStream, DataConsumer<T> action) throws IOException {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(dataOutputStream);
        Objects.requireNonNull(action);
        dataOutputStream.writeInt(collection.size());
        for (T t : collection) {
            action.accept(t);
        }
    }

    @Override
    public Resume read(InputStream inputStream) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            Resume resume = new Resume(dataInputStream.readUTF(), dataInputStream.readUTF());
            readWithException(dataInputStream, () -> resume.addContact(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF()));
            readWithException(dataInputStream, () -> {
                SectionType sectionType = SectionType.valueOf(dataInputStream.readUTF());
                resume.addSection(sectionType, readSection(sectionType, dataInputStream));
            });
            return resume;
        }
    }

    private AbstractSection readSection(SectionType sectionType, DataInputStream dataInputStream) throws IOException {
        switch (sectionType) {
            case OBJECTIVE, PERSONAL -> {
                return new TextSection(dataInputStream.readUTF());
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                return new ListSection(readList(dataInputStream, dataInputStream::readUTF));
            }
            case EXPERIENCE, EDUCATION -> {
                return new CompanySection(readList(dataInputStream, () -> new Company(new Link(dataInputStream.readUTF(), readNull(dataInputStream)),
                        readList(dataInputStream, () -> new Company.Period(getLocalDate(dataInputStream), getLocalDate(dataInputStream),
                                dataInputStream.readUTF(), readNull(dataInputStream))))));
            }
            default -> throw new StorageException("section read error");
        }
    }

    private <T> List<T> readList(DataInputStream dataInputStream, DataSupplier<T> supplier) throws IOException {
        int size = dataInputStream.readInt();
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    private void readWithException(DataInputStream dataInputStream, DataProcessor processor) throws IOException {
        int size = dataInputStream.readInt();
        for (int i = 0; i < size; i++) {
            processor.process();
        }
    }

    private static LocalDate getLocalDate(DataInputStream dataInputStream) throws IOException {
        return LocalDate.parse(dataInputStream.readUTF());
    }

    private static String readNull(DataInputStream dataInputStream) throws IOException {
        String s = dataInputStream.readUTF();
        if (!s.equals(NULL_HOLDER)) {
            return s;
        } else {
            return null;
        }
    }
}
