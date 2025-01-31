package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CompanySection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private List<Company> companies;

    public CompanySection() {

    }

    public CompanySection(List<Company> companies) {
        Objects.requireNonNull(companies, "companies must not be null");
        this.companies = companies;
    }

    public CompanySection(Company... companies) {
        this(Arrays.asList(companies));
    }

    public List<Company> getCompanies() {
        return companies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;

        return Objects.equals(companies, that.companies);
    }

    @Override
    public int hashCode() {
        return companies != null ? companies.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CompanySection{" +
                "companies=" + companies +
                "} " + "\n";
    }
}
