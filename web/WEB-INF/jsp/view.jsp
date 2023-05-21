<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>

<body>
<jsp:include page="fragments/header.jsp"/>

<section>
    <h2>${resume.fullName}&nbsp; <a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.AbstractSection>"/>
            <c:set var="sectionName" value="${sectionEntry.key.name()}"/>
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>

    <p><b> ${type.title}:</b></p>

    <c:choose>
        <c:when test="${type == 'OBJECTIVE' || type == 'PERSONAL'}">
            <%=((TextSection) section).getContent()%>
        </c:when>

        <c:when test="${type == 'ACHIEVEMENT' || type =='QUALIFICATIONS'}">
            <ul>
                <c:forEach var="item" items="<%= ((ListSection)sectionEntry.getValue()).getItems()%>">
                    <li><c:out value="${item}"/></li>
                </c:forEach>
            </ul>
        </c:when>

        <c:when test="${type == 'EXPERIENCE' || type == 'EDUCATION'}">

            <c:forEach var="company" items="<%= ((CompanySection)sectionEntry.getValue()).getCompanies()%>">
                <p>
                <table class="table-two">

                    <tr>
                        <th></th>
                        <th><b> <a href="${company.homepage.url}">${company.homepage.name}</a> </b></th>
                    </tr>

                    <c:forEach var="period" items="${company.periods}">
                        <tr>
                            <td>
                                <span>${DateUtil.dateFormat(period.startDate)} - ${DateUtil.dateFormat(period.endDate)}</span>
                            </td>
                            <td>
                                <span><b><em>${period.title}</em></b></span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <span>${period.description}</span>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                </p>
            </c:forEach>
        </c:when>

    </c:choose>
    </c:forEach>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
