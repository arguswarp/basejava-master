package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.*;

public class HtmlUtil {
    public static String sectionToHtml(SectionType type, AbstractSection section) {
        StringBuilder message = new StringBuilder();
        switch (type) {
            case PERSONAL, OBJECTIVE -> {
                TextSection textSection = (TextSection) section;
                message.append(textSection.getContent());
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                ListSection listSection = (ListSection) section;
                message.append(String.join("<br/>", listSection.getItems()));
            }
            case EXPERIENCE, EDUCATION -> {
                CompanySection companySection = (CompanySection) section;
                for (Company c : companySection.getCompanies()) {
                    message.append(c.getHomepage().getName())
                            .append("</br>")
                            .append(c.getHomepage().getUrl())
                            .append("</br>");
                    c.getPeriods().forEach(period -> message.append(period.getTitle()).append(period.getDescription()));
                }
            }
            default -> throw new IllegalStateException("Section type is invalid");
        }
        return "<b>" + type.getTitle() + "</b>" + ":" + "<br/>" + message + "<br/>";
    }
}
