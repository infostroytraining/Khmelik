<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome!</title>
</head>
<body>

<div>
    Welcome ${sessionScope.user.name} ${sessionScope.user.surname}!
</div>
<br/>
<a href="logout">Log out</a>

</body>
</html>
