<%@ page import="java.util.Optional" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page import="ru.javawebinar.basejava.model.*" %>
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
            <dd><input name="fullName" type="text"
                       pattern="^(?![' -])[a-zA-Zа-яА-ЯёЁ'-]{1,28}(?<![' -])(?:\s(?! )(?![ '-])\s*[a-zA-Zа-яА-ЯёЁ'-]{1,28}(?<![ '-]))*$"
                       required size="30" value="${resume.fullName}"></dd>
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
            <%--            TODO: add editable sections--%>
            <c:when test="${sectionType == 'EXPERIENCE' || sectionType =='EDUCATION'}">
                <c:if test="<%=resume.getSections().containsKey(sectionType)%>">

                        <span>Добавить компанию:</span>
                    <p>
                        <input id="newName" type="text" name="${sectionType.name()}" size="66" placeholder="Название компании"> <br/>
                        <input type="text" name="${sectionType.name()}newCompanyUrl" size="66" placeholder="Сcылка"> <br/>
                        <input type="text" name="${sectionType.name()}newCompanyPeriodStart" size="30" placeholder="Начало работы">
                        <input type="text" name="${sectionType.name()}newCompanyPeriodEnd" size="30" placeholder="Конец работы"> <br/>
                        <input type="text" name="${sectionType.name()}newCompanyPeriodTitle" size="66" placeholder="Должность"> <br/>
                        <input type="text" name="${sectionType.name()}newCompanyPeriodDescription" size="66" placeholder="Описание">
                    </p>
                    <hr/>
                    <c:forEach var="company"
                               items="<%=((CompanySection)resume.getSections().get(sectionType)).getCompanies()%>">
                        <p>
                            <input type="text" name="${sectionType.name()}" size="66" value="${company.homepage.name}"> <br/>
                            <input type="text" name="${company.homepage.url}" size="66" value="${company.homepage.url}"> <br/>
                        </p>

                        <span>Добавить должность:</span>
                        <p>
                            <input type="text" name="${company.homepage.name}newPeriodStart" size="30" placeholder="Начало работы">
                            <input type="text" name="${company.homepage.name}newPeriodEnd" size="30" placeholder="Конец работы"> <br/>
                            <input type="text" name="${company.homepage.name}newPeriodTitle" size="66" placeholder="Должность"> <br/>
                            <input type="text" name="${company.homepage.name}newPeriodDescription" size="66" placeholder="Описание">
                        </p>
                        <span>Редактировать должность:</span>
                            <c:forEach var="companyPeriod" items="${company.periods}">
                                <p>
                                <input type="text" name="${companyPeriod.title}StartDate" size="30" value="${DateUtil.dateFormat(companyPeriod.startDate)}">
                                <input type="text" name="${companyPeriod.title}EndDate" size="30" value="${DateUtil.dateFormat(companyPeriod.endDate)}"> <br/>
                                <input type="text" name="${companyPeriod.title}Title" size="66" value="${companyPeriod.title}"> <br/>
                                <input type="text" name="${companyPeriod.title}Description" size="66" value="${companyPeriod.description}">
                                </p>
                            </c:forEach>

                    </c:forEach>

                </c:if>

                <c:if test="<%=!resume.getSections().containsKey(sectionType)%>">
                    <p>
                        <span>Новая компания</span>
                        <br/>
                        <input type="text" name="${sectionType.name()}" size="30" value="">
                        <input type="text" name="${sectionType.name()}newCompanyUrl" size="30" value="Сайт">
                        <input type="text" name="${sectionType.name()}newCompanyPeriodStart" size="30" value="Начало работы">
                        <input type="text" name="${sectionType.name()}newCompanyPeriodEnd" size="30" value="Конец работы">
                        <input type="text" name="${sectionType.name()}newCompanyPeriodTitle" size="30" value="Должность">
                    </p>
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
