<%--
  Created by IntelliJ IDEA.
  User: we0z
  Date: 07.03.18
  Time: 11:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>edit meal</title>
</head>
<body>
<form action="meals" method="post">
    <input type="hidden" name="id" value="${meal.id}">
    <labeL>Date: <input type="datetime-local" name="dateTime" value="${meal.dateTime}"/></labeL>  <br>
    <label>Desc: <input type="text" name="description" value="${meal.description}"/></label> <br>
    <label>Calories: <input type="number" name="calories" value="${meal.calories}"></label> <br>
    <input type="submit" value="Save">
</form>
</body>
</html>
