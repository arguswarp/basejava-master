package ru.javawebinar.basejava.storage.serialization;

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
        try {
            dataOutputStream.writeUTF(s);
        } catch (NullPointerException e) {
            dataOutputStream.writeUTF(NULL_HOLDER);
        }
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
            int contactsSize = dataInputStream.readInt();
            for (int i = 0; i < contactsSize; i++) {
                resume.addContact(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF());
            }
            int sectionsSize = dataInputStream.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType sectionType = SectionType.valueOf(dataInputStream.readUTF());
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL ->
                            resume.addSection(sectionType, new TextSection(dataInputStream.readUTF()));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> textList = new ArrayList<>();
                        int textListSize = dataInputStream.readInt();
                        for (int j = 0; j < textListSize; j++) {
                            textList.add(dataInputStream.readUTF());
                        }
                        resume.addSection(sectionType, new ListSection(textList));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        List<Company> companies = new ArrayList<>();
                        int companiesSize = dataInputStream.readInt();
                        for (int j = 0; j < companiesSize; j++) {
                            String name = dataInputStream.readUTF();
                            String url = readNull(dataInputStream);
                            List<Company.Period> periods = new ArrayList<>();
                            int periodsSize = dataInputStream.readInt();
                            for (int k = 0; k < periodsSize; k++) {
                                periods.add(new Company.Period(getLocalDate(dataInputStream),
                                        getLocalDate(dataInputStream),
                                        dataInputStream.readUTF(), readNull(dataInputStream)));
                            }
                            companies.add(new Company(new Link(name, url), periods));
                        }
                        resume.addSection(sectionType, new CompanySection(companies));
                    }
                }
            }
            return resume;
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
