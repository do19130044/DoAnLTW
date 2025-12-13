
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Tech Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>

<!-- Header -->
<jsp:include page="includes/header.jsp" />

<!-- Navigation Bar -->
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />

<!-- Login Form -->

<section class="content login-page">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h2 class="text-center mb-4"><fmt:message key="login.title" /></h2>
                    <c:if test="${not empty error}">
                        <p style="color: red;">${error}</p>
                    </c:if>
                    <form action="login" method="POST">
                        <div class="mb-3">
                            <label for="loginUsername" class="form-label"><fmt:message key="login.username" /></label>
                            <input type="text" class="form-control" id="loginUsername" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="loginPassword" class="form-label"><fmt:message key="login.password" /></label>
                            <input type="password" class="form-control" id="loginPassword" name="password" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100"><fmt:message key="login.title" /></button>
                    </form>
                    <p class="mt-3"><fmt:message key="login.text1" /><a href="register"><fmt:message key="login.link1" /></a>.</p>
                    <p class="mt-3"><fmt:message key="login.text2" /> <a href="forgot-password"><fmt:message key="login.link2" /></a>.</p>

                </div>
            </div>
        </div>
    </div>
</section>
<!-- <section class="container mt-5">
    <h2 class="text-center">Login</h2>
    <form>
        <div class="mb-3">
            <label for="email" class="form-label">Email address</label>
            <input type="email" class="form-control" id="email" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" class="form-control" id="password" required>
        </div>
        <button type="submit" class="btn btn-primary">Login</button>
    </form>
    <p class="mt-3">Don't have an account? <a href="register.html">Register here</a>.</p>
</section> -->

<!-- Footer -->
<jsp:include page="includes/footer.jsp" />


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist"/>

<script src="app.js"></script>
</body>

</html>

