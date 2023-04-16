package ru.javawebinar.basejava.model;

import com.google.gson.annotations.JsonAdapter;
import ru.javawebinar.basejava.util.DateUtil;
import ru.javawebinar.basejava.util.JsonLocalDateAdapter;
import ru.javawebinar.basejava.util.XmlLocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    private Link homepage;
    private List<Period> periods;

    public Company() {

    }


    public Company(Link homepage, List<Period> periods) {
        Objects.requireNonNull(homepage, "homepage must not be null");
        Objects.requireNonNull(periods, "periods must not be null");
        this.homepage = homepage;
        this.periods = periods;
    }

    public Company(String name, String url, List<Period> periods) {
        this(new Link(name, url), periods);
    }

    public Company(String name, String url, Period... periods) {
        this(new Link(name, url), Arrays.asList(periods));
    }

    public Link getHomepage() {
        return homepage;
    }

    public void setHomepage(Link homepage) {
        this.homepage = homepage;
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
                '}' + "\n";
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Period implements Serializable {
        private static final long serialVersionUID = 1L;
        @JsonAdapter(JsonLocalDateAdapter.class)
        @XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
        private LocalDate startDate;
        @JsonAdapter(JsonLocalDateAdapter.class)
        @XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
        private LocalDate endDate;
        private String title;
        private String description;

        public Period() {

        }

        public Period(LocalDate startDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(startDate, "start date must not be null");
            Objects.requireNonNull(endDate, "end date must not be null");
            Objects.requireNonNull(title, "title must not be null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }

        public Period(int startMonth, int startYear, int endMonth, int endYear, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth), title, description);
        }

        public Period(Month startMonth, int startYear, Month endMonth, int endYear, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth), title, description);
        }

        public Period(int startMonth, int startYear, int endMonth, int endYear, String title) {
            this(startMonth, startYear, endMonth, endYear, title, null);
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Period period = (Period) o;

            if (!startDate.equals(period.startDate)) return false;
            if (!endDate.equals(period.endDate)) return false;
            if (!title.equals(period.title)) return false;
            return Objects.equals(description, period.description);
        }

        @Override
        public int hashCode() {
            int result = startDate.hashCode();
            result = 31 * result + endDate.hashCode();
            result = 31 * result + title.hashCode();
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Period{" +
                    "startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}' + "\n";
        }
    }
}
