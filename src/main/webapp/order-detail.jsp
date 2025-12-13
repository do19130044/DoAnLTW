<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details - Tech Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>
<!-- Header -->
<jsp:include page="includes/header.jsp" />


<!-- Main Content -->
<section class="content">
<div class="container mt-5">
    <h2 class="text-center"><fmt:message key="order.detail.title" /></h2>

    <!-- Thông tin đơn hàng -->
    <div class="row">
        <div class="col-md-6">
            <h4><fmt:message key="checkout.summary" /></h4>
            <p><strong><fmt:message key="order.code" />:</strong> ${order.orderId}</p>
            <p><strong><fmt:message key="order.status" />:</strong> ${order.status}</p>
            <p><strong><fmt:message key="order.price" />:</strong> <fmt:formatNumber value="${order.totalPrice}" pattern="#,##0.00" /> VND</p>
            <p><strong><fmt:message key="order.address" />:</strong> ${order.shippingAddress}</p>
        </div>
        <div class="col-md-6">
            <h4><fmt:message key="order.detail.customer" /></h4>
            <p><strong><fmt:message key="user.username" />:</strong> ${order.customer.username}</p>
            <p><strong><fmt:message key="user.email" />:</strong> ${order.customer.email}</p>
            <p><strong><fmt:message key="user.phone" />:</strong> ${order.customer.phone}</p>
        </div>
    </div>

    <!-- Danh sách sản phẩm -->
    <div class="mt-4">
        <h4><fmt:message key="order.detail.product" /></h4>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>#</th>
                <th><fmt:message key="cart.products" /></th>
                <th><fmt:message key="cart.price" /></th>
                <th><fmt:message key="cart.quantity" /></th>
                <th><fmt:message key="cart.total" /></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${orderItems}">
                <tr>
                    <td>${item.product.productId}</td>
                    <td>${item.product.name}</td>
                    <td><fmt:formatNumber value="${item.price}" minFractionDigits="0" maxFractionDigits="0" /> <span class="currency">VND</span></td>
                    <td>${item.quantity}</td>
                    <td><fmt:formatNumber value="${item.quantity * item.price}" pattern="#,##0.00" /> VND</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</section>

<!-- Footer -->
<jsp:include page="includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
