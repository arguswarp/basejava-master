package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class Company {
    private final String name;
    private final String url;
    private final List<Period> periods;

    public Company(String name, String url, List<Period> periods) {
        this.name = name;
        this.url = url;
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        return Objects.equals(periods, company.periods);
    }

    @Override
    public int hashCode() {
        return periods != null ? periods.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' + "\n" +
                ", periods=" + periods +
                '}';
    }
}
