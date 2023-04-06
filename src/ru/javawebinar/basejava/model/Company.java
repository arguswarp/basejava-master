package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.DateUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
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
        this(new Link(name,url),periods);
    }

    public Company(String name, String url, Period ... periods) {
        this(new Link(name,url), Arrays.asList(periods));
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
                '}' +"\n";
    }

    public static class Period {
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String title;
        private final String description;

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
            this(DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth),title,description);
        }

        public Period(Month startMonth, int startYear, Month endMonth, int endYear, String title, String description) {
            this(DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth),title,description);
        }

        public Period(int startMonth, int startYear, int endMonth, int endYear, String title) {
           this(startMonth, startYear,endMonth,endYear,title,"");
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
