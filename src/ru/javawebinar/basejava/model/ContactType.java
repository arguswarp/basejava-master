package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Тел."),
    ADDRESS("Проживание"),
    MAIL("mail"),
    SKYPE("Skype"),
    LINKEDIN(""),
    GITHUB(""),
    STACKOVERFLOW(""),
    PERSONAL_SITE("");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
