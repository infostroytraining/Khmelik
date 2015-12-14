<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>
<head>
    <title>Welcome!</title>
</head>
<body>
<div>
    Welcome ${sessionScope.user.name} ${sessionScope.user.surname}!
    <br/>
    <c:if test="${not empty sessionScope.user.image}">
        <img src="../images/${sessionScope.user.image}"/>
    </c:if>
</div>
<br/>
<a href="logout">Log out</a>

</body>
</html>
