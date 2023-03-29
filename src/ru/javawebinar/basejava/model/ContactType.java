package ru.javawebinar.basejava.model;

public enum ContactType {
    ADDRESS("Проживание"),
    MAIL("mail"),
    SKYPE("Skype"),
    PROFILES(""),
    PHONE("Тел.");

    private String title;
    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
