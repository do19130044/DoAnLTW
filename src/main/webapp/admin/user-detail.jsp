<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Người Dùng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<!-- Header -->
<jsp:include page="admin_header.jsp" />

<div class="container mt-5">
    <!-- Quản lý Người Dùng -->
    <div class="d-flex justify-content-between mb-3">
        <h2>Thông tin Người Dùng</h2>
        <a class="btn btn-primary" href="user-detail">Quay lại danh sách người dùng</a>
    </div>

    <!-- Thông tin người dùng -->
    <table class="table table-bordered">
        <tbody>
        <tr>
            <th>Mã người dùng</th>
            <td>${user.id}</td>
        </tr>
        <tr>
            <th>Tên người dùng</th>
            <td>${user.username}</td>
        </tr>
        <tr>
            <th>Email</th>
            <td>${user.email}</td>
        </tr>
        <tr>
            <th>Số điện thoại</th>
            <td>${user.phone}</td>
        </tr>
        <tr>
            <th>Vai trò</th>
            <td>${user.role}</td>

        </tbody>
    </table>

    <!-- Hành động -->
    <div class="d-flex justify-content-end">
        <a href="user-detail?action=edit&userId=${user.id}" class="btn btn-warning">Chỉnh sửa</a>
        <form action="user-detail" method="POST" class="d-inline"  onsubmit="return confirmDelete()">
            <input type="hidden" name="userId" value="${user.id}">
            <input type="hidden" name="action" value="delete">
            <button type="submit" class="btn btn-danger ms-2">Xóa</button>
        </form>
    </div>
</div>

<jsp:include page="admin_footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<!-- Thêm script xác nhận xóa -->
<script>
    // Hàm xác nhận xóa
    function confirmDelete() {
        return confirm("Bạn có chắc chắn muốn xóa người dùng này?");
    }
</script>
</body>
</html>

