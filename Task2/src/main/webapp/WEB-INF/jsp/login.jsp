<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>
<head>
    <title>LogIn</title>
    <link rel="stylesheet" type="text/css" href="css/registration.css">
</head>
<body>
<form action="login" method="post">
    <fieldset>
        <legend>Log in</legend>
        Email
        <br/>
        <input name="email" type="email" value="${sessionScope.loginDTO}"/>
        <br/><br/>
        Password
        <br/>
        <input name="password" type="password"/>
        <br/><br/>
        <input type="submit" value="Log In">
        <a href="registration">Registration...</a>
        <br/>
        <span style="color: red">${sessionScope.loginError}</span>
        <c:remove var="loginError" scope="session"/>
    </fieldset>
</form>
</body>
</html>
