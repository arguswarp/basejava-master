package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.*;

import java.util.Optional;

public class HtmlUtil {
    public static String sectionToHtml(SectionType type, AbstractSection section) {
        StringBuilder message = new StringBuilder();
        message.append("<br/>");
        switch (type) {
            case PERSONAL, OBJECTIVE -> {
                TextSection textSection = (TextSection) section;
                message.append("<br/>")
                        .append(textSection.getContent())
                        .append("</br>");
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                ListSection listSection = (ListSection) section;
                message.append("<br/>")
                        .append(String.join("<br/>", listSection.getItems()))
                        .append("</br>");
            }
            case EXPERIENCE, EDUCATION -> {
                CompanySection companySection = (CompanySection) section;
                for (Company c : companySection.getCompanies()) {
                    Optional<String> url = Optional.ofNullable(c.getHomepage().getUrl());
                    message.append("</br>")
                            .append(c.getHomepage().getName())
                            .append(url.map(s -> "</br>").orElse(""))
                            .append(url.map(s -> "<a href ='" + s + "'>" + s + "</a>").orElse(""))
                            .append("</br>");
                    c.getPeriods().forEach(period -> {
                        Optional<String> description = Optional.ofNullable(period.getDescription());
                        message.append(period.getStartDate()).append(" - ").append(period.getEndDate())
                                .append(" ")
                                .append(period.getTitle())
                                .append(description.map(s ->"</br>" ).orElse(""))
                                .append(description.orElse(""))
                                .append("</br>");
                    });
                }
            }
            default -> throw new IllegalStateException("Section type is invalid");
        }
        return "<b>" + type.getTitle() + "</b>" + ":" + message;
    }
}
