package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ResumeServlet extends HttpServlet {

    protected Storage storage; // = Config.get().getStorage();


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume resume;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "add":
                resume = new Resume();
                request.setAttribute("resume", resume);
                request.getRequestDispatcher(
                        ("/WEB-INF/jsp/edit.jsp")
                ).forward(request, response);
                return;
            case "view":
            case "edit":
                resume = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume;
        if (Objects.equals(uuid, "")) {
            resume = new Resume(fullName);
            storage.save(resume);
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String[] value = request.getParameterValues(type.name());
            String value0 = value[0];
            if ((value0 != null && value0.trim().length() != 0)) {
                switch (type) {
                    case OBJECTIVE, PERSONAL -> resume.addSection(type, new TextSection(value0));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> list = Arrays.stream(value0.replaceAll("\r", "")
                                        .split("\n"))
                                .filter(s -> !s.equals(""))
                                .toList();
                        resume.addSection(type, new ListSection(list));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        String newName = value[value.length - 1];
                        String newUrl = request.getParameter(type + "newCompanyUrl");
                        String startDate = request.getParameter(type + "newCompanyPeriodStart");
                        String endDate = request.getParameter(type + "newCompanyPeriodEnd");
                        String title = request.getParameter(type + "newCompanyPeriodTitle");
                        String description = request.getParameter(type + "newCompanyPeriodDescription");
                        List<Company> companyList = ((CompanySection) resume.getSections().get(type)).getCompanies();
                        List<Company> updatedList = new ArrayList<>();
                        int i = 0;
                        for (Company company : companyList) {
                            String oldName = company.getHomepage().getName();
                            String updatedName = value[i++];
                            String updatedUrl = request.getParameter(company.getHomepage().getUrl());
                            String updatedStartDate = request.getParameter(oldName + "StartDate");
                            String updatedEndDate = request.getParameter(oldName + "EndDate");
                            String updatedTitle = request.getParameter(oldName + "Title");
                            String updatedDescription = request.getParameter(oldName + "Description");
                            Company updatedCompany = new Company(updatedName, updatedUrl, new Company.Period(DateUtil.of(updatedStartDate), DateUtil.of(updatedEndDate), updatedTitle, updatedDescription));
                            updatedList.add(updatedCompany);
                        }
                        if (newName != null && newName.trim().length() != 0) {
                            updatedList.add(new Company(newName, newUrl, new Company.Period(DateUtil.of(startDate), DateUtil.of(endDate), title, description)));
                        }
                        resume.addSection(type, new CompanySection(updatedList));
                    }
                }
            }
            else {
                resume.getSections().remove(type);
            }
        }
        storage.update(resume);
        response.sendRedirect("resume");
    }
}
