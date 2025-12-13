<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile - Tech Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>

<!-- Header -->

<!-- Navigation Bar -->
<jsp:include page="includes/header.jsp" />
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />
<!-- User Profile Section -->
<section class="container mt-5">
    <h2 class="text-center"><fmt:message key="user.title" /></h2>
<c:if test="${not empty sessionScope.user}">
<div class="row justify-content-center">
        <div class="col-md-6">
            <c:if test="${not empty error}">
                <p style="color: red">${error}</p>
            </c:if>
            <form>
                <div class="mb-3">
                    <label for="fullName" class="form-label"><fmt:message key="user.username" /></label>
                    <input type="text" class="form-control" id="fullName" value="${sessionScope.user.username}"  readonly>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label"><fmt:message key="user.email" /></label>
                    <input type="email" class="form-control" id="email" value="${sessionScope.user.email}" readonly>
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label"><fmt:message key="user.phone" /></label>
                    <input type="text" class="form-control" id="phone" value="${sessionScope.user.phone}" readonly>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label"><fmt:message key="user.password" /></label>
                    <input type="password" class="form-control" id="password" value="********" readonly>
                </div>
                <button class="btn btn-primary" type="button" data-bs-toggle="modal" data-bs-target="#editModal"><fmt:message key="user.edit" /></button>
                <button class="btn btn-secondary ms-2" type="button" data-bs-toggle="modal" data-bs-target="#changePasswordModal"><fmt:message key="user.change_pass" /></button>
            </form>
        </div>
    </c:if>

    <c:if test="${empty sessionScope.user}">
            <p class="text-center text-danger">You are not logged in. Please log in to view your profile.</p>
        </c:if>
    </div>
</section>
<!-- Modal thông báo lỗi -->
<div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="errorModalLabel">Error</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <!-- Hiển thị thông báo lỗi ở đây -->
                <c:if test="${not empty error}">
                    <p>${error}</p>
                </c:if>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message key="user.close" /></button>
            </div>
        </div>
    </div>
</div>

<!-- Edit Profile Modal -->
<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editModalLabel"><fmt:message key="user.edit" /></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="editProfileForm" method="post" action="">
                    <div class="mb-3">
                        <label for="editFullName" class="form-label"><fmt:message key="user.username" /></label>
                        <input type="text" class="form-control" id="editFullName" name="username" value="${sessionScope.user.username}" required>
                    </div>
                    <div class="mb-3">
                        <label for="editEmail" class="form-label"><fmt:message key="user.email" /></label>
                        <input type="email" class="form-control" id="editEmail" name="email" value="${sessionScope.user.email}" required>
                    </div>
                    <div class="mb-3">
                        <label for="editPhone" class="form-label"><fmt:message key="user.phone" /></label>
                        <input type="text" class="form-control" id="editPhone" name="phone" value="${sessionScope.user.phone}" >
                    </div>
                    <input type="hidden" name="userId" value="${sessionScope.user.id}">
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message key="user.close" /></button>
                <button type="submit" form="editProfileForm" class="btn btn-primary"><fmt:message key="user.save" /></button>
            </div>
        </div>
    </div>
</div>

<!-- Change Password Modal -->
<!-- Change Password Modal -->
<div class="modal fade" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="changePasswordModalLabel"><fmt:message key="user.change_pass" /></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="changePasswordForm" action="user_change_password" method="post">
                    <div class="mb-3">
                        <label for="oldPassword" class="form-label"><fmt:message key="user.old_pass" /></label>
                        <input type="password" class="form-control" id="oldPassword" name="oldPassword" required>
                    </div>
                    <button type="submit" class="btn btn-primary"><fmt:message key="user.submit" /></button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="includes/footer.jsp" />


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="app.js"></script>
</body>

</html>
