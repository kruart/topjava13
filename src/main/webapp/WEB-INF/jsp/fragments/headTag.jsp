<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="app.title"/></title>

    <%--https://stackoverflow.com/questions/2204870/how-to-get-domain-url-and-application-name/2206865#2206865--%>
    <c:set var="url">${pageContext.request.requestURL}</c:set>
    <c:set var="uri">${pageContext.request.requestURI}</c:set>
    <base href="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${pageContext.request.contextPath}/">

    <link rel="stylesheet" href="resources/css/style.css">
</head>