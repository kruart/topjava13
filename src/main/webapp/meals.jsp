<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>

    <style>
        .exceed {
            color: red;
        }

        .normal {
            color: green;
        }

        table, th, td {
            border: black solid 1px;
        }
    </style>
</head>
<body>
    <table>
        <tr>
            <th>datetime</th>
            <th>description</th>
            <th>calories</th>
            <th>exceed</th>
        </tr>


        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
            <tr class="${meal.exceed == true ? 'exceed' : 'normal'}">
                <td>
                    <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                    <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}"/>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>${meal.exceed}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
