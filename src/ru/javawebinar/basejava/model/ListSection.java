package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    private final List<String> textList;

    public ListSection(List<String> textList) {
        this.textList = textList;
    }

    public List<String> getTextList() {
        return textList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return Objects.equals(textList, that.textList);
    }

    @Override
    public int hashCode() {
        return textList != null ? textList.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ListSection{" +
                "textList=" + textList +
                "} " + super.toString();
    }
}
