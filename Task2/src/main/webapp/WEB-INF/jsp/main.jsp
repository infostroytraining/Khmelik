<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">

    <title>Infostroy - Task 5 (Bootstrap & Ajax)</title>

    <!-- Bootstrap Core CSS -->
    <link rel="stylesheet" href="resources/css/external/bootstrap.min.css" type="text/css">

    <!-- Custom Fonts -->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Merriweather:400,300,300italic,400italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>

    <!-- Plugin CSS -->
    <link rel="stylesheet" href="resources/css/external/animate.min.css" type="text/css">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="resources/css/external/creative.css" type="text/css">
    <link rel="stylesheet" href="resources/css/form-elements.css" type="text/css">

    <link rel="stylesheet" href="resources/css/external/snow.css">

</head>

<body id="page-top">
<div id="flake">&#10052;</div>

<nav id="mainNav" class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">

        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand page-scroll" href="#page-top">Start</a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a class="page-scroll" href="#about">About</a>
                </li>
                <c:if test="${not empty sessionScope.user.email}">
                <li>
                    <a class="page-scroll" href="#user">User</a>
                </li>
                <li id="logout">
                    <a id="btn-logout" class="page-scroll" href="logout">Logout</a>
                </li>
                </c:if>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>

<header>
    <div class="header-content">
        <div class="header-content-inner">
            <h1>Welcome to Task 5, stranger!</h1>
            <hr>
            <p>Technologies used: jQuery, Ajax, HTML5, CSS3, Bootstrap</p>
        </div>
    </div>
</header>

<section class="bg-primary" id="about">
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-lg-offset-2 text-center">
                <h2 class="section-heading">We've got what you need!</h2>
                <hr class="light">
                <p class="text-faded">Just register an account or sign in, if u already have one!
                    <br/>Let us show you, how glad we are that you`ve come!
                </p>
            <c:if test="${empty sessionScope.user}">
                <a class="btn btn-default btn-xl wow tada btn-signreg" data-toggle="modal" data-target="#signInModal">Sign in</a>
                <a class="btn btn-default btn-xl wow tada btn-signreg" data-toggle="modal" data-target="#registerModal">Register</a>
            </c:if>
            </div>
        </div>
    </div>
</section>

<section class="bg-primary hidden" id="user">
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-lg-offset-2 text-center">
                <h2 class="section-heading">User info</h2>
                <hr class="light">
                <p class="text-faded" id="userInfo"></p>
            </div>
        </div>
    </div>
</section>


<%@include file="/WEB-INF/jspf/modals/registrationModal.jspf"%>
<%@include file="/WEB-INF/jspf/modals/loginModal.jspf"%>

<%@include file="/WEB-INF/jspf/footer.jspf"%>

<script src="resources/js/external/snow.js"></script>

</body>

</html>
