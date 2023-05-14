package ru.javawebinar.basejava.model;

import java.util.Optional;

public enum ContactType {
    PHONE("Тел."),
    ADDRESS("Проживание"),
    MAIL("mail") {
        @Override
        public String toHtml0(String value) {
            return "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    LINKEDIN("Linkedin"),
    GITHUB("Github"),
    STACKOVERFLOW("Stackoverflow"),
    PERSONAL_SITE("Личный сайт");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String toHtml(String value) {
        return Optional.ofNullable(value).isPresent() ? toHtml0(value) : "";
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }

}
