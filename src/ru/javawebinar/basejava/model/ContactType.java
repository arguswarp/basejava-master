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
    LINKEDIN("Linkedin"){
        @Override
        public String toHtml0(String value) {
            return "<a href='" + value + "'>" + value + "</a>";
        }
    },
    GITHUB("Github"){
        @Override
        public String toHtml0(String value) {
            return "<a href='" + value + "'>" + value + "</a>";
        }
    },
    STACKOVERFLOW("Stackoverflow"){
        @Override
        public String toHtml0(String value) {
            return "<a href='" + value + "'>" + value + "</a>";
        }
    },
    PERSONAL_SITE("Личный сайт"){
        @Override
        public String toHtml0(String value) {
            return "<a href='" + value + "'>" + value + "</a>";
        }
    };

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
