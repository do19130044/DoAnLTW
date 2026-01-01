
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Listing - Tech Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<style>
    .card {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        height: 100%; /* Đảm bảo các thẻ có chiều cao bằng nhau */
    }

    .card img {
        object-fit: contain;
        height: 200px; /* Cố định chiều cao cho ảnh để đồng đều */
    }

    .card-body {
        flex-grow: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    .discount-badge {
        position: absolute;
        top: 10px;
        right: 10px;
        background-color: #ff6f61;
        color: white;
        padding: 5px 10px;
        border-radius: 5px;
        display: flex;
        align-items: center;
        font-size: 0.8em;
    }

</style>

<body>

<!-- Header -->
<jsp:include page="includes/header.jsp" />

<!-- Category Filter Section -->
<!-- Search and Sort Form -->
<div class="container mt-5">
    <form action="product" method="get" class="d-flex justify-content-center align-items-center gap-3">
        <!-- Tìm kiếm sản phẩm -->
        <div class="col-md-6">
            <input type="hidden" name="search" class="form-control" placeholder="Tìm kiếm sản phẩm..." value="${search}">
        </div>

        <!-- Sắp xếp theo giá -->
        <div class="col-md-4">
            <select name="sort" class="form-select">
                <option value="asc" ${sort == 'asc' ? 'selected' : ''}><fmt:message key="product.asc" /></option>
                <option value="desc" ${sort == 'desc' ? 'selected' : ''}><fmt:message key="product.desc" /></option>
            </select>
        </div>

        <!-- Nút tìm kiếm -->
        <div class="col-md-2" style="width: 130px">
            <input type="submit" value="<fmt:message key="product.filter" />" class="btn btn-primary w-100">
        </div>
    </form>
</div>

<!-- Product List -->
<section class="container mt-5">

    <h2 class="text-center"><fmt:message key="product.all_product" /></h2>
    <div class="row row-cols-1 row-cols-md-4 g-4 mt-4">
        <c:forEach var="product" items="${prod}">

            <div class="col-md-3">
                <div class="card h-100">
                    <img src="${pageContext.request.contextPath}/img/${product.imagePath}" alt="image" loading="lazy">
                    <div class="card-body">
                        <h5 class="card-title">${product.name}</h5>

                        <!-- Giá sau khi giảm giá -->
                        <p class="card-text">
                            <fmt:formatNumber value="${product.price-(product.price * (product.discountPercentage/100))}" minFractionDigits="0" maxFractionDigits="0"/>
                            <span class="currency">VND</span>
                        </p>

                        <!-- Giá gốc với chữ nhỏ và dấu gạch ngang -->
                        <!-- Kiểm tra nếu có giảm giá (discountPercentage > 0) để hiển thị giá gốc -->
                        <c:if test="${product.discountPercentage > 0}">
                            <p class="card-text text-muted" style="text-decoration: line-through; font-size: 0.8em;">
                                <fmt:formatNumber value="${product.price}" minFractionDigits="0" maxFractionDigits="0"/>
                                <span class="currency">VND</span>
                            </p>
                        </c:if>

                        <!-- Giảm giá phần trăm với mũi tên đi xuống -->
                        <div class="discount-badge" style="position: absolute; top: 10px; right: 10px; background-color: #ff6f61; color: white; padding: 5px 10px; border-radius: 5px; display: flex; align-items: center;">
                            <span>${product.discountPercentage}%</span>
                            <span style="margin-left: 5px; font-size: 10px; transform: rotate(45deg);">&#x2193;</span> <!-- Mũi tên đi xuống -->
                        </div>

                        <a href="productdetail?id=${product.productId}"  class="btn btn-primary"><fmt:message key="product.view_detail" /></a>
                    </div>
                </div>
            </div>

        </c:forEach>
    </div>
</section>


        <!-- Add more products similarly -->
<div class="container mt-4">
            <nav>
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item"><a class="page-link" href="?page=${currentPage - 1}&search=${param.search}&category=${param.category}">Trang trước</a></li>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="?page=${i}&search=${param.search}&category=${param.category}">${i}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item"><a class="page-link" href="?page=${currentPage + 1}&search=${param.search}&category=${param.category}">Trang sau</a></li>
                    </c:if>
                </ul>
            </nav>
</div>



<!-- Footer -->
<jsp:include page="includes/footer.jsp" />


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="app.js"></script>
</body>

</html>

