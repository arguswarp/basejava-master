package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class Company {
    private final Link homepage;
    private final List<Period> periods;

    public Company(Link homepage, List<Period> periods) {
        Objects.requireNonNull(homepage, "homepage must not be null");
        Objects.requireNonNull(periods, "periods must not be null");
        this.homepage = homepage;
        this.periods = periods;
    }
    public Company(String name, String url, List<Period> periods) {
        homepage = new Link(name,url);
        Objects.requireNonNull(periods, "periods must not be null");
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

        if (!homepage.equals(company.homepage)) return false;
        return periods.equals(company.periods);
    }

    @Override
    public int hashCode() {
        int result = homepage.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
                "homepage=" + homepage +
                ", periods=" + periods +
                '}';
    }
}
