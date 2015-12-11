<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User registration</title>
    <link rel="stylesheet" type="text/css" href="css/registration.css">
</head>
<body>

<h3>Register your account, please!</h3>

<div>
    <form action="registration" method="post" enctype="multipart/form-data">

        <label>Email *</label>
        <br/>
        <input name="email" type="email" value="${sessionScope.userDTO.email}"/>
        <hr/>

        <label>Name *</label>
        <br/>
        <input name="name" type="text" value="${sessionScope.userDTO.name}"/>
        <hr/>

        <label>Surname *</label>
        <br/>
        <input name="surname" type="text" value="${sessionScope.userDTO.surname}"/>
        <hr/>

        <label>Password *</label>
        <br/>
        <input name="password" type="password" value=""/>
        <hr/>

        <label>Confirm password *</label>
        <br/>
        <input name="confirmedPassword" type="password" value=""/>
        <hr/>

        <label>Avatar (Image)</label>
        <br/>
        <input name="image" type="file"/>
        <hr/>

        <label>I am not robot</label>
        <!-- CAPCHA HERE -->
        <hr/>

        <p>Fields with * are required.</p>

        <input type="submit" value="Register!"/>
    </form>
</div>

<!-- Field validation exceptions -->
<div>
    <span style="color: red;">
        <c:forEach items="${sessionScope.fieldExceptions}" var="error">
            ${error.message}<br/>
        </c:forEach>
        ${sessionScope.userAlreadyExistsException.message}
        <c:remove var="fieldExceptions" scope="session"/>
        <c:remove var="userAlreadyExistsException" scope="session"/>
        <c:remove var="userDTO" scope="session"/>
    </span>
</div>

</body>
</html>
