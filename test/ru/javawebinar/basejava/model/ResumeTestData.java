package ru.javawebinar.basejava.model;

import java.util.List;

public class ResumeTestData {
    public static Resume createFilledResume(String uuid, String name) {
        Resume resume = new Resume(uuid, name);

        resume.addContact(ContactType.PHONE, "+7(921) 855-0482");
        resume.addContact(ContactType.SKYPE, "skype:grigory.kislin");
//        resume.addContact(ContactType.MAIL, " gkislin@yandex.ru");
//        resume.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
//        resume.addContact(ContactType.GITHUB, "https://github.com/gkislin");
//        resume.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
//        resume.addContact(ContactType.PERSONAL_SITE, "http://gkislin.ru/");
//
        resume.addSection(SectionType.OBJECTIVE, new TextSection("Bедущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры"));
//
//        List<String> achievementList = new ArrayList<>();
//        achievementList.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет");
//        achievementList.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.");
//        achievementList.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
////        achievementList.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
////        achievementList.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
////        achievementList.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
////        achievementList.add("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
////
//        resume.addSection(SectionType.ACHIEVEMENT, new ListSection(achievementList));
////
//        List<String> qualificationList = new ArrayList<>();
//        qualificationList.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
//        qualificationList.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
////        qualificationList.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB");
////        qualificationList.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
////        qualificationList.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
////        qualificationList.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements)");
////        qualificationList.add("Python: Django");
////        qualificationList.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
////        qualificationList.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
////        qualificationList.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT");
////        qualificationList.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
////        qualificationList.add("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer");
////        qualificationList.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования");
////        qualificationList.add("Родной русский, английский \"upper intermediate\"");
////
//        resume.addSection(SectionType.QUALIFICATIONS, new ListSection(qualificationList));
//
//        List<Company> companies = new ArrayList<>();
//
//        Company company1 = createCompany("Java Online Projects", "http://javaops.ru/",
//                new Company.Period(DateUtil.of(2013, Month.OCTOBER), LocalDate.now(), "Автор проекта",
//                        "Создание, организация и проведение Java онлайн проектов и стажировок"));
//        Company company2 = createCompany("Wrike", "https://www.wrike.com/",
//                new Company.Period(10, 2014, 10, 2016,
//                        "Старший разработчик (backend)",
//                        "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO"));
//        Company company3 = createCompany("RIT Center", null,
//                new Company.Period(4, 2012, 10, 2014,
//                        "Java архитектор",
//                        "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"));
//        Company company4 = createCompany("Luxoft (Deutsche Bank)", "http://www.luxoft.ru/",
//                new Company.Period(12, 2010, 4, 2012,
//                        "Ведущий программист",
//                        "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5."));
//        Company company5 = createCompany("Yota", "https://www.yota.ru/",
//                new Company.Period(6, 2008, 12, 2010,
//                        "Ведущий специалист",
//                        "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)"));
//        Company company6 = createCompany("Enkata", "http://enkata.com/",
//                new Company.Period(3, 2007, 6, 2008,
//                        "Разработчик ПО",
//                        "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining)."));
//        Company company7 = createCompany("Siemens AG", "https://www.siemens.com/ru/ru/home.html",
//                new Company.Period(1, 2005, 2, 2007,
//                        "Разработчик ПО",
//                        "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix)."));
//
//        Company company8 = createCompany("Alcatel", "http://www.alcatel.ru/",
//                new Company.Period(9, 1997, 1, 2005,
//                        "Инженер по аппаратному и программному тестированию",
//                        "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM)."));
//
//        companies.add(company1);
//        companies.add(company2);
//        companies.add(company3);
//        companies.add(company4);
//        companies.add(company5);
//        companies.add(company6);
//        companies.add(company7);
//        companies.add(company8);
//
//        resume.addSection(SectionType.EXPERIENCE, new CompanySection(companies));
//
//        List<Company> educations = new ArrayList<>();
//
//        Company education1 = createCompany("Coursera", "https://www.coursera.org/course/progfun",
//                new Company.Period(3, 2013, 5, 2013,
//                        "'Functional Programming Principles in Scala' by Martin Odersky"));
//        Company education2 = createCompany("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366",
//                new Company.Period(3, 2011, 4, 2011,
//                        "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'"));
//        Company education3 = createCompany("Siemens AG", "http://www.siemens.ru/",
//                new Company.Period(3, 2011, 4, 2011,
//                        "3 месяца обучения мобильным IN сетям (Берлин)"));
//        Company education4 = createCompany("Alcatel", "http://www.alcatel.ru/",
//                new Company.Period(1, 2005, 4, 2005,
//                        "6 месяцев обучения цифровым телефонным сетям (Москва)"));
//        Company education5 = createCompany("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
//                "http://www.ifmo.ru/",
//                new Company.Period(9, 1993, 7, 1996,
//                        "Аспирантура (программист С, С++)"),
//                new Company.Period(9, 1987, 7, 1993,
//                        "Инженер (программист Fortran, C)"));
//        Company education6 = createCompany("Заочная физико-техническая школа при МФТИ", "https://mipt.ru/",
//                new Company.Period(9, 1984, 6, 1987,
//                        "Закончил с отличием"));
//
//        educations.add(education1);
//        educations.add(education2);
//        educations.add(education3);
//        educations.add(education4);
//        educations.add(education5);
//        educations.add(education6);
//
//        resume.addSection(SectionType.EDUCATION, new CompanySection(educations));

        return resume;
    }

    public static Company createCompany(String name, String url, Company.Period... periods) {
        return new Company(name, url, List.of(periods));
    }

    public static void main(String[] args) {
        Resume resume = createFilledResume("123456789", "Григорий Кислин");
        System.out.println(resume);
    }
}
