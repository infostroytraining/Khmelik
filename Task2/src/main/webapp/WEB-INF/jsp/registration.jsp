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


<div>
    <form action="registration" method="post" enctype="multipart/form-data">
        <h4>Register your account, please!</h4>

        <p style="color: red">Fields with * are required.</p>

        <div>
            <label>Email *</label>
            <br/>
            <input name="email" type="email" value="${sessionScope.userDTO.email}"/>
        </div>

        <div>
            <label>Name *</label>
            <br/>
            <input name="name" type="text" value="${sessionScope.userDTO.name}"/>
        </div>

        <div>
            <label>Surname *</label>
            <br/>
            <input name="surname" type="text" value="${sessionScope.userDTO.surname}"/>
        </div>

        <div>
            <label>Password *</label>
            <br/>
            <input name="password" type="password" value=""/>
        </div>

        <div>
            <label>Confirm password *</label>
            <br/>
            <input name="confirmedPassword" type="password" value=""/>
        </div>

        <div>
            <label>Avatar (Image)</label>
            <br/>
            <input name="image" type="file"/>
        </div>

        <%@include file="/WEB-INF/jspf/captcha/googleRecaptcha.jspf" %>

        <%@include file="/WEB-INF/jspf/captcha/simpleCaptcha.jspf" %>

        <hr/>

        <div>
            <input type="submit" value="Register!"/>
        </div>
    </form>
</div>

<!-- Field validation exceptions -->
<div>
    <span style="color: red;">
        <c:forEach items="${sessionScope.fieldExceptions}" var="error">
            ${error.message}<br/>
        </c:forEach>
        ${sessionScope.userAlreadyExistsException.message}
        ${sessionScope.captchaValidationException.message}

        <c:remove var="captchaValidationException" scope="session"/>
        <c:remove var="fieldExceptions" scope="session"/>
        <c:remove var="userAlreadyExistsException" scope="session"/>
        <c:remove var="userDTO" scope="session"/>
    </span>
</div>

</body>
</html>
