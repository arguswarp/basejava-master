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
                    <textarea rows="6" cols="50" name="${sectionType.name()}"><%=((TextSection) resume.getSections().get(sectionType)).getContent()%></textarea>
                </c:if>
                <c:if test="<%=!resume.getSections().containsKey(sectionType)%>">
                     <textarea rows="6" cols="50" name="${sectionType.name()}"></textarea>
                </c:if>
            </c:when>

            <c:when test="${sectionType == 'ACHIEVEMENT' || sectionType =='QUALIFICATIONS'}">
                <c:if test="<%=resume.getSections().containsKey(sectionType)%>">
                    <textarea rows="6" cols="50" name="${sectionType.name()}"><%=String.join("\n", ((ListSection) resume.getSections().get(sectionType)).getItems())%></textarea>
                </c:if>
                <c:if test="<%=!resume.getSections().containsKey(sectionType)%>">
                     <textarea rows="6" cols="50" name="${sectionType.name()}"></textarea>
                </c:if>
            </c:when>
            <%--            TODO: add editable sections--%>
            <c:when test="${sectionType == 'EXPERIENCE' || sectionType =='EDUCATION'}">
               <c:set var="datePattern" value="([\d]{2}\/[\d]{4})|[Сс]ейчас" />
               <c:set var="namePlaceholder" value="Название" />
               <c:set var="urlPlaceholder" value="Сcылка" />
               <c:set var="startDatePlaceholder" value="Начало - MM/YYYY" />
               <c:set var="endDatePlaceholder" value="Конец - MM/YYYY" />
               <c:set var="titlePlaceholder" value="Заголовок" />
               <c:set var="descriptionPlaceholder" value="Описание" />

                <c:if test="<%=resume.getSections().containsKey(sectionType)%>">
                        <span>Добавить место:</span>
                    <p>
                        <input type="text" name="${sectionType.name()}newCompanyName" size="66" placeholder="${namePlaceholder}"> <br/>
                        <input type="text" name="${sectionType.name()}newCompanyUrl" size="66" placeholder="${urlPlaceholder}"> <br/>
                        <input type="text" pattern="${datePattern}" name="${sectionType.name()}newCompanyPeriodStart" size="30" placeholder="${startDatePlaceholder}">
                        <input type="text" pattern="${datePattern}" name="${sectionType.name()}newCompanyPeriodEnd" size="30" placeholder="${endDatePlaceholder}"> <br/>
                        <input type="text" name="${sectionType.name()}newCompanyPeriodTitle" size="66" placeholder="${titlePlaceholder}"> <br/>
                        <textarea rows="4" cols="62" name="${sectionType.name()}newCompanyPeriodDescription" placeholder="${descriptionPlaceholder}"></textarea>
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
                            <input type="text" pattern="${datePattern}" name="${company.homepage.name}newPeriodStart" size="30" placeholder="${startDatePlaceholder}">
                            <input type="text" pattern="${datePattern}" name="${company.homepage.name}newPeriodEnd" size="30" placeholder="${endDatePlaceholder}"> <br/>
                            <input type="text" name="${company.homepage.name}newPeriodTitle" size="66" placeholder="${titlePlaceholder}"> <br/>
                            <textarea rows="4" cols="62" name="${company.homepage.name}newPeriodDescription" placeholder="${descriptionPlaceholder}"></textarea>
                        </p>
                        <span>Редактировать должность:</span>
                            <c:forEach var="companyPeriod" items="${company.periods}">
                                <p>
                                <input type="text" pattern="${datePattern}" name="${companyPeriod.title}StartDate" size="30" value="${DateUtil.dateFormat(companyPeriod.startDate)}">
                                <input type="text" pattern="${datePattern}" name="${companyPeriod.title}EndDate" size="30" value="${DateUtil.dateFormat(companyPeriod.endDate)}"> <br/>
                                <input type="text" name="${companyPeriod.title}Title" size="66" value="${companyPeriod.title}"> <br/>
                                    <textarea rows="4" cols="62" name="${companyPeriod.title}Description">${companyPeriod.description}</textarea>
                                </p>
                            </c:forEach>
                        <hr/>
                    </c:forEach>
                </c:if>
                <c:if test="<%=!resume.getSections().containsKey(sectionType)%>">
                    <span>Добавить место:</span>
                    <p>
                        <input type="text" name="${sectionType.name()}" size="66" placeholder="${namePlaceholder}"> <br/>
                        <input type="text" name="${sectionType.name()}newCompanyUrl" size="66" placeholder="${urlPlaceholder}"> <br/>
                        <input type="text" pattern="${datePattern}" name="${sectionType.name()}newCompanyPeriodStart" size="30" placeholder="${startDatePlaceholder}">
                        <input type="text" pattern="${datePattern}" name="${sectionType.name()}newCompanyPeriodEnd" size="30" placeholder="${endDatePlaceholder}"> <br/>
                        <input type="text" name="${sectionType.name()}newCompanyPeriodTitle" size="66" placeholder="${titlePlaceholder}"> <br/>
                        <textarea rows="4" cols="62" name="${sectionType.name()}newCompanyPeriodDescription" placeholder="${descriptionPlaceholder}"></textarea>
                    </p>
                    <hr/>
                </c:if>
            </c:when>
        </c:choose>
        </c:forEach>

        <button type="submit">Сохранить</button>
        <br/>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
