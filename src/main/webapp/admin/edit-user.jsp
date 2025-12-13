<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa Người Dùng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<!-- Header -->
<jsp:include page="admin_header.jsp" />

<div class="container mt-5">
    <!-- Chỉnh sửa Người Dùng -->
    <div class="d-flex justify-content-between mb-3">
        <h2>Chỉnh sửa thông tin người dùng</h2>
        <a class="btn btn-primary" href="user-detail?userId=${user.id}">Quay lại danh sách người dùng</a>
    </div>

    <!-- Form chỉnh sửa thông tin người dùng -->
    <form action="user-detail" method="POST">
        <input type="hidden" name="userId" value="${user.id}">
        <input type="hidden" name="action" value="update">
        <div class="mb-3">
            <label for="username" class="form-label">Tên người dùng</label>
            <input type="text" class="form-control" id="username" name="username" value="${user.username}" readonly required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email" value="${user.email}" required>
        </div>
        <div class="mb-3">
            <label for="phone" class="form-label">Số điện thoại</label>
            <input type="text" class="form-control" id="phone" name="phone" value="${user.phone}" required>
        </div>
        <div class="mb-3">
            <label for="role" class="form-label">Vai trò</label>
            <select class="form-control" id="role" name="role" required>
                <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                <option value="CUSTOMER" ${user.role == 'CUSTOMER' ? 'selected' : ''}>User</option>
            </select>
        </div>
        <button type="submit" class="btn btn-success">Lưu thay đổi</button>
    </form>
</div>

<jsp:include page="admin_footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

