<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>
<head>
    <title>User registration</title>
    <link rel="stylesheet" type="text/css" href="css/registration.css">
    <script src='https://www.google.com/recaptcha/api.js'></script>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script src="js/registration.js" type="application/javascript"></script>
</head>
<body>


<form action="registration" method="post" enctype="multipart/form-data">
    <fieldset>
        <legend>Register your account, please!</legend>
        Email *<br/>
        <input name="email" type="email" value="${sessionScope.userDTO.email}"/>
        <br/><br/>
        Name *<br/>
        <input name="name" type="text" value="${sessionScope.userDTO.name}"/>
        <br/><br/>
        Surname *<br/>
        <input name="surname" type="text" value="${sessionScope.userDTO.surname}"/>
        <br/><br/>
        Password *<br/>
        <input name="password" type="password" value=""/>
        <br/><br/>
        Confirm password *<br/>
        <input name="confirmedPassword" type="password" value=""/>
        <br/><br/>
        Avatar (Image)<br/>
        <input name="image" type="file"/>
        <br/><br/>
    </fieldset>
    <fieldset>
        <legend>Anti-robot protection</legend>

        <%@include file="/WEB-INF/jspf/captcha/googleRecaptcha.jspf" %>
        <br/>
        <%@include file="/WEB-INF/jspf/captcha/simpleCaptcha.jspf" %>

    </fieldset>

    <fieldset>
        <legend>Submit and validation errors</legend>
            <span style="color: red;">
            <c:if test="${not empty sessionScope.validationException.fieldExceptions}">
                <c:forEach items="${sessionScope.validationException.fieldExceptions}" var="error">
                    ${error.message}<br/>
                </c:forEach>
            </c:if>
            ${sessionScope.userInputException.message}
            ${sessionScope.transactionException.message}
            <c:remove var="validationException" scope="session"/>
            <c:remove var="captchaDuplicationException" scope="session"/>
            <c:remove var="transactionException" scope="session"/>
            <input type="submit" value="Register!"/>
            <a href="login">Sign In</a>
    </span>

    </fieldset>
</form>


</body>
</html>
