<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password - Tech Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

</head>

<body>

<jsp:include page="includes/header.jsp" />
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />

<section class="content forgot-password-page">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h2 class="text-center mb-4"><fmt:message key="forgot-pass.title" /></h2>

                    <!-- Hiển thị thông báo -->
                    <c:if test="${not empty error}">
                        <p style="color: red;">${error}</p>
                    </c:if>
                    <c:if test="${not empty message}">
                        <p style="color: green;">${message}</p>
                    </c:if>

                    <form action="forgot-password" method="POST">
                        <div class="mb-3">
                            <label for="email" class="form-label"><fmt:message key="forgot-pass.email" /></label>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100"><fmt:message key="forgot-pass.submit" /></button>
                    </form>
                    <p class="mt-3"><a href="login"><fmt:message key="forgot-pass.login" /></a></p>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="includes/footer.jsp" />
</body>
</html>
