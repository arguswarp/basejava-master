<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.TextSection" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>

<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input name="fullName" type="text" pattern="^(?![' -])[a-zA-Zа-яА-ЯёЁ'-]{1,28}(?<![' -])(?:\s(?! )(?![ '-])\s*[a-zA-Zа-яА-ЯёЁ'-]{1,28}(?<![ '-]))*$" required size="30" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3

        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size="30" value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>

        <p>
            <c:forEach var="sectionType" items="<%=SectionType.values()%>">
                <jsp:useBean id="sectionType" type="ru.javawebinar.basejava.model.SectionType"/>

        <p><b> ${sectionType.title}:</b></p>
        <c:choose>
            <c:when test="${sectionType == 'OBJECTIVE' || sectionType == 'PERSONAL'}">
                <c:if test="<%=resume.getSections().containsKey(sectionType)%>">
                    <textarea rows="6" cols="50" name="${sectionType.name()}"
                              id="content"><%=((TextSection) resume.getSections().get(sectionType)).getContent()%></textarea>
                </c:if>
                <c:if test="<%=!resume.getSections().containsKey(sectionType)%>">
                     <textarea rows="6" cols="50" name="${sectionType.name()}"
                               id="content"></textarea>
                </c:if>
            </c:when>

            <c:when test="${sectionType == 'ACHIEVEMENT' || sectionType =='QUALIFICATIONS'}">
                <c:if test="<%=resume.getSections().containsKey(sectionType)%>">
                    <textarea rows="6" cols="50" name="${sectionType.name()}"
                              id="content"><%=String.join("\n", ((ListSection) resume.getSections().get(sectionType)).getItems())%></textarea>
                </c:if>
                <c:if test="<%=!resume.getSections().containsKey(sectionType)%>">
                     <textarea rows="6" cols="50" name="${sectionType.name()}"
                               id="content"></textarea>
                </c:if>
            </c:when>
        </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <br/>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
