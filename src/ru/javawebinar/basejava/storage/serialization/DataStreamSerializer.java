package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    private static final String NULL_HOLDER = "THIS_IS_NULL_HOLDER";

    @Override
    public void write(Resume resume, OutputStream outputStream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());

            Map<ContactType, String> contacts = resume.getContacts();
            dataOutputStream.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                dataOutputStream.writeUTF(entry.getKey().name());
                dataOutputStream.writeUTF(entry.getValue());
            }

            Map<SectionType, AbstractSection> sections = resume.getSections();
            dataOutputStream.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                dataOutputStream.writeUTF(entry.getKey().name());
                AbstractSection section = entry.getValue();
                switch (entry.getKey()) {
                    case OBJECTIVE, PERSONAL -> {
                        TextSection textSection = (TextSection) section;
                        dataOutputStream.writeUTF(textSection.getContent());
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ListSection listSection = (ListSection) section;
                        List<String> items = listSection.getItems();
                        dataOutputStream.writeInt(items.size());
                        for (String item : items) {
                            dataOutputStream.writeUTF(item);
                        }
                    }
                    case EXPERIENCE, EDUCATION -> {
                        CompanySection companySection = (CompanySection) section;
                        List<Company> companies = companySection.getCompanies();
                        dataOutputStream.writeInt(companies.size());
                        for (Company company : companies) {
                            dataOutputStream.writeUTF(company.getHomepage().getName());
                            writeNull(company.getHomepage().getUrl(), dataOutputStream);
                            List<Company.Period> periods = company.getPeriods();
                            dataOutputStream.writeInt(periods.size());
                            for (Company.Period period : periods) {
                                dataOutputStream.writeUTF(period.getStartDate().toString());
                                dataOutputStream.writeUTF(period.getEndDate().toString());
                                dataOutputStream.writeUTF(period.getTitle());
                                writeNull(period.getDescription(), dataOutputStream);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void writeNull(String s, DataOutputStream dataOutputStream) throws IOException {
        try {
            dataOutputStream.writeUTF(s);
        } catch (NullPointerException e) {
            dataOutputStream.writeUTF(NULL_HOLDER);
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
