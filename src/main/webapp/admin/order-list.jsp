<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quản lý Đơn Hàng</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

  <link rel="stylesheet" href="style.css">
</head>
<body>
<!-- Header -->

<jsp:include page="admin_header.jsp" />

<div class="container mt-5">
  <!-- Dropdown Quản lý đơn hàng -->
  <li class="nav-item dropdown">
    <a class="nav-link dropdown-toggle" href="#" id="manageDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
      Quản lý Đơn hàng
    </a>
    <ul class="dropdown-menu" aria-labelledby="manageDropdown">
      <li><a class="dropdown-item" href="order-list">Quản lý Đơn hàng</a></li>
      <li><a class="dropdown-item" href="home">Quản lý Sản phẩm</a></li>
    </ul>
  </li>

  <!-- Tìm kiếm và Hiển thị tài khoản -->

  <!-- Bảng danh sách đơn hàng -->
  <table class="table table-bordered">
    <thead>
    <tr>
      <th>Mã đơn hàng</th>
      <th>Người dùng</th>
      <th>Địa chỉ giao hàng</th>
      <th>Giá trị đơn hàng</th>
      <th>Trạng thái</th>
      <th>Ngày tạo</th>
      <th>Hành động</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="order" items="${orders}">
      <tr>
        <td>${order.orderId}</td>
        <td>${order.customer.username}</td>
        <td>${order.shippingAddress}</td>
        <td>${order.totalPrice}</td>
        <td>${order.status}</td>
        <td>${order.orderDate}</td>
        <td>
          <c:choose>
            <c:when test="${order.status == 'PENDING'}">
              <!-- Nếu trạng thái là PENDING, hiển thị nút Đồng ý và Huỷ -->
              <form action="order-list" method="POST" class="d-inline">
                <input type="hidden" name="id" value="${order.orderId}">
                <input type="hidden" name="action" value="acceptOrder">
                <input type="hidden" name="page" value="${currentPage}">

                <button type="submit" class="btn btn-success">Chấp nhận</button>
              </form>
              <form action="order-list" method="POST" class="d-inline">
                <input type="hidden" name="id" value="${order.orderId}">
                <input type="hidden" name="action" value="cancelOrder">
                <input type="hidden" name="page" value="${currentPage}">

                <button type="submit" class="btn btn-danger">Từ chối</button>
              </form>
            </c:when>
            <c:when test="${order.status == 'DELIVERED'}">
              <!-- Nếu trạng thái là DELIVERED, hiển thị nút 'Đã giao hàng' -->
              <form action="order-list" method="POST" class="d-inline">
                <input type="hidden" name="id" value="${order.orderId}">
                <input type="hidden" name="action" value="shipOrder">
                <input type="hidden" name="page" value="${currentPage}">

                <button type="submit" class="btn btn-info">Đã giao hàng</button>
              </form>
            </c:when>
            <c:when test="${order.status == 'SHIPPED'}">
              <!-- Nếu trạng thái là SHIPPED, hiển thị nút 'Xoá' -->
              <form action="order-list" method="POST" class="d-inline">
                <input type="hidden" name="id" value="${order.orderId}">
                <input type="hidden" name="action" value="deleteOrder">
                <input type="hidden" name="page" value="${currentPage}">

                <button type="submit" class="btn btn-danger">Xoá đơn hàng</button>
              </form>
            </c:when>
            <c:when test="${order.status == 'CANCELLED'}">
              <!-- Nếu trạng thái là CANCELLED, hiển thị thông báo Huỷ đơn hàng -->
              <form action="order-list" method="POST" class="d-inline">
                <input type="hidden" name="id" value="${order.orderId}">
                <input type="hidden" name="action" value="deleteOrder">
                <input type="hidden" name="page" value="${currentPage}">

                <button type="submit" class="btn btn-danger">Xoá đơn hàng</button>
              </form>
            </c:when>
          </c:choose>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  <!-- Phân trang -->
  <nav>
    <ul class="pagination">
      <c:if test="${currentPage > 1}">
        <li class="page-item"><a class="page-link" href="order-list?page=${currentPage - 1}">Trang trước</a></li>
      </c:if>
      <c:forEach var="i" begin="1" end="${totalPages}">
        <li class="page-item ${i == currentPage ? 'active' : ''}">
          <a class="page-link" href="order-list?page=${i}">${i}</a>
        </li>
      </c:forEach>
      <c:if test="${currentPage < totalPages}">
        <li class="page-item"><a class="page-link" href="order-list?page=${currentPage + 1}">Trang sau</a></li>
      </c:if>
    </ul>
  </nav>
</div>

<jsp:include page="admin_footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
