<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Tech Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        .error {
            color: red;
            display: none;
        }
        .success {
            color: green;
            display: none;
        }
        .eye-icon {
            position: absolute;
            right: 28px;
            top: 49%;
            transform: translateY(-50%);
            cursor: pointer;
        }
    </style>
</head>

<body>
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />
<!-- Header -->
<jsp:include page="includes/header.jsp" />

<!-- Navigation Bar -->
<!--<nav class="navbar navbar-expand-lg navbar-light bg-light">-->
<!--    <div class="container">-->
<!--        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"-->
<!--                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">-->
<!--            <span class="navbar-toggler-icon"></span>-->
<!--        </button>-->
<!--        <div class="collapse navbar-collapse" id="navbarNav">-->
<!--            <ul class="navbar-nav mx-auto">-->
<!--                <li class="nav-item">-->
<!--                    <a class="nav-link active" href="index.html">Home</a>-->
<!--                </li>-->
<!--                <li class="nav-item dropdown">-->
<!--                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button">Products</a>-->
<!--                    <ul class="dropdown-menu">-->
<!--                        <li><a class="dropdown-item" href="#">Laptops</a></li>-->
<!--                        <li><a class="dropdown-item" href="#">Smartphones</a></li>-->
<!--                        <li><a class="dropdown-item" href="#">Accessories</a></li>-->
<!--                    </ul>-->
<!--                </li>-->
<!--                <li class="nav-item">-->
<!--                    <a class="nav-link" href="login.html">Login</a>-->
<!--                </li>-->
<!--                <li class="nav-item">-->
<!--                    <a class="nav-link" href="contact.html">Contact</a></li>-->
<!--            </ul>-->
<!--        </div>-->
<!--    </div>-->
<!--</nav>-->

<!-- Registration Form -->
<section class="register-page content" >
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h2 class="text-center mb-4"><fmt:message key="register.title" /></h2>

                    <!-- Hiển thị thông báo lỗi nếu có -->
                    <c:if test="${not empty error}">
                        <p style="color: red;">${error}</p>
                    </c:if>
                    <form id="registerForm" action="register" method="POST">
                        <div class="mb-3">
                            <label for="registerName" class="form-label"><fmt:message key="register.username" /></label>
                            <input type="text" class="form-control" id="registerName" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="registerEmail" class="form-label">Email </label>
                            <input type="email" class="form-control" id="registerEmail" name="email" required>
                        </div>
                        <div class="mb-3">
                            <label for="registerPassword" class="form-label"><fmt:message key="register.password" /></label>
                            <input type="password" class="form-control" id="registerPassword" name="password" required>
                            <span class="eye-icon" id="togglePassword">
                                👁️
                             </span>
                        </div>
                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label"><fmt:message key="register.re_password" /></label>
                            <input type="password" class="form-control" id="confirmPassword" required>

                            <p id="errorMessage" class="error"><fmt:message key="register.false_pass" /></p>
                            <p id="successMessage" class="success"><fmt:message key="register.true_pass" /></p>
                        </div>
                        <div class="mb-3">
                            <label for="registerPhone" class="form-label"><fmt:message key="register.phone" /></label>
                            <input type="number" class="form-control" id="registerPhone" name="phone" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100"><fmt:message key="register.title" /></button>
                    </form>
                    <p class="mt-3"><fmt:message key="register.text1" /> <a href="login"><fmt:message key="register.link1" /></a>.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Footer -->
<jsp:include page="includes/footer.jsp" />


<script>
    const passwordInput = document.getElementById('registerPassword');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const errorMessage = document.getElementById('errorMessage');
    const successMessage = document.getElementById('successMessage');
    const form = document.getElementById('registerForm');
    const togglePassword = document.getElementById('togglePassword');



    // Bật/tắt hiển thị mật khẩu
    togglePassword.addEventListener('click', function() {
        // Kiểm tra nếu mật khẩu đang ở chế độ 'password' (ẩn)
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';  // Hiển thị mật khẩu
        } else {
            passwordInput.type = 'password';  // Ẩn mật khẩu
        }
    });



    form.addEventListener('submit', function(event) {
        // event.preventDefault();

        if (passwordInput.value !== confirmPasswordInput.value) {
            errorMessage.style.display = 'block';
            successMessage.style.display = 'none';
            event.preventDefault();

        } else {
            errorMessage.style.display = 'none';
            successMessage.style.display = 'block';
        }
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist"/>

<script src="app.js"></script>
</body>

</html>

