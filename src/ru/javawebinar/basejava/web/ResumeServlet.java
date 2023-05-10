package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResumeServlet extends HttpServlet {

    private Storage storage;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write("<html>" +
                "<style> table, th, td {border: 1px solid black}</style>" +
                "<body>" +
                "<table>" +
                "<tr>" +
                "<th>uuid</th>" +
                "<th>full name</th>" +
                "</tr>");
        storage.getAllSorted().forEach(resume -> printWriter.write("<tr>" +
                "<td>" + resume.getUuid() + "</td>" +
                "<td>" + resume.getFullName() + "</td>" +
                "</tr>"
        ));
        printWriter.write("</table>" +
                "</body>" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
