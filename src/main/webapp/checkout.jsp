<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CheckOut - Tech Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>

<!-- Header -->
<jsp:include page="includes/header.jsp" />
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />

<!-- Checkout Section -->
<section class="content">
<div class="container mt-5">
    <h2 class="text-center"><fmt:message key="checkout.title" /></h2>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
    <form action="checkout" method="post">
        <div class="row">
            <div class="col-md-6">
                <h4><fmt:message key="checkout.bill" /></h4>
                <div class="mb-3">
                    <label for="address" class="form-label"><fmt:message key="checkout.address" /></label>
                    <input type="text" class="form-control" id="address" name="shippingAddress" required>
                </div>

                <!-- Hidden input for user ID -->
                <input type="hidden" name="userId" value="${sessionScope.user.id}" />

                <!-- Hidden input for order status -->
                <input type="hidden" name="status" value="Pending" />

                <!-- Hidden input for order date (current timestamp) -->
                <input type="hidden" name="orderDate" value="<%= new java.sql.Timestamp(System.currentTimeMillis()) %>" />
            </div>

            <div class="col-md-6">
                <h4><fmt:message key="checkout.summary" /></h4>
                <ul class="list-group mb-3">
                    <c:forEach var="item" items="${cart.items}">
                        <c:set var="prod" value="${item}" />
                        <li class="list-group-item d-flex justify-content-between">
                            <span>${prod.product.name}</span>
                            <strong><fmt:formatNumber value="${prod.product.price-(prod.product.price * (prod.product.discountPercentage/100))}" minFractionDigits="0" maxFractionDigits="0"/> <span class="currency">VND</span></strong>

                        </li>
                        <li class="list-group-item d-flex justify-content-between">
                            <span><fmt:message key="cart.quantity" /></span>
                            <strong>${prod.quantity}</strong>
                        </li>
                    </c:forEach>
                    <li class="list-group-item d-flex justify-content-between">
                        <span><fmt:message key="cart.total" /> (VND)</span>
                        <strong>
                            <fmt:formatNumber value="${cart.total}" minFractionDigits="0" maxFractionDigits="0" />
                            <span class="currency">VND</span>
                        </strong>
                    </li>
                </ul>
                <button type="submit" class="btn btn-primary btn-lg btn-block"><fmt:message key="checkout.order" /></button>
            </div>
        </div>
    </form>
</div>
</section>

<!-- Footer -->
<jsp:include page="includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="app.js"></script>
</body>

</html>
