<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="topjava" tagdir="/WEB-INF/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <%--@elvariable id="userTo" type="ru.javawebinar.topjava.to.UserTo"--%>
        <div class="row">
            <div class="col-5 offset-3">
                <h3><c:if test="${not register}">${userTo.name}</c:if> <spring:message code="${register ? 'app.register' : 'app.profile'}"/></h3>
                <form:form modelAttribute="userTo" class="form-horizontal" method="post" action="${register ? 'register' : 'profile'}"
                           charset="utf-8" accept-charset="UTF-8">

                    <topjava:inputField labelCode="user.name" name="name"/>
                    <topjava:inputField labelCode="user.email" name="email"/>
                    <topjava:inputField labelCode="user.password" name="password" inputType="password"/>
                    <topjava:inputField labelCode="user.caloriesPerDay" name="caloriesPerDay" inputType="number"/>
                    
                    <div class="g-recaptcha" data-sitekey="6Lfb1GoUAAAAAFGWEbPbq79cJZap2a69D4YcfpdP"></div>
                    <c:if test="${not empty  captchaSuccess}">
                        <div class="alert alert-danger">
                            <spring:message code="${captchaSuccess}"/>
                        </div>
                    </c:if>

                    <div class="text-right">
                        <button type="submit" class="btn btn-primary">
                            <span class="fa fa-check"></span>
                            <spring:message code="common.save"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
        <sec:authorize access="isAnonymous()">
            <div class="row">
                <div class=" col-5 offset-3">
                    <jsp:include page="fragments/social.jsp"/>
                </div>
            </div>
        </sec:authorize>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>