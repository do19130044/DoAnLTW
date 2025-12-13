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
    <title>Modern Tech Shop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<style>
    .product-slider {
        position: relative;
        margin-top: 20px;

    }

    .product-item {
        display: none; /* Hide all products initially */
        margin-bottom: 20px;
    }

    #prevBtn, #nextBtn {
        margin: 0 10px;
        padding: 10px 20px;
        cursor: pointer;
    }

    #prevBtn:hover, #nextBtn:hover {
        background-color: #ddd;
    }

    @media (max-width: 768px) {
        .product-item {
            display: inline-block;
            width: 100%; /* On smaller screens, show one product per slide */
        }
    }

    .card {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        height: 100%; /* Đảm bảo các thẻ có chiều cao bằng nhau */
    }

    .card img {
        object-fit: cover;
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

</style>
</head>

<body>

<!-- Include Header -->
<jsp:include page="includes/header.jsp" />


<!-- Sidebar for login and register -->
<div id="loginSidebar" class="sidebar">
    <div class="sidebar-content">
        <h2>Login</h2>
        <form>
            <div class="mb-3">
                <label for="loginEmail" class="form-label">Email address</label>
                <input type="email" class="form-control" id="loginEmail" required>
            </div>
            <div class="mb-3">
                <label for="loginPassword" class="form-label">Password</label>
                <input type="password" class="form-control" id="loginPassword" required>
            </div>
            <button type="submit" class="btn btn-primary">Login</button>
        </form>
        <button class="btn btn-danger closeSidebar" id="closeLoginSidebar">Close</button>
    </div>
</div>

<div id="registerSidebar" class="sidebar">
    <div class="sidebar-content">
        <h2>Register</h2>
        <form>
            <div class="mb-3">
                <label for="registerName" class="form-label">Full Name</label>
                <input type="text" class="form-control" id="registerName" required>
            </div>
            <div class="mb-3">
                <label for="registerEmail" class="form-label">Email address</label>
                <input type="email" class="form-control" id="registerEmail" required>
            </div>
            <div class="mb-3">
                <label for="registerPassword" class="form-label">Password</label>
                <input type="password" class="form-control" id="registerPassword" required>
            </div>
            <button type="submit" class="btn btn-primary">Register</button>
        </form>
        <button class="btn btn-danger closeSidebar" id="closeRegisterSidebar">Close</button>
    </div>
</div>

<!-- Hero Slider -->
<div id="heroSlider" class="carousel slide" data-bs-ride="carousel" data-bs-interval="3000">
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="img/banner1.webp" class="d-block w-100" alt="Banner 1">
        </div>
        <div class="carousel-item " >
            <img src="img/banner2.webp" class="d-block w-100" alt="Banner 2">
        </div>
        <div class="carousel-item ">
            <img src="img/banner3.webp" class="d-block w-100" alt="Banner 3">
        </div>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#heroSlider" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden"> <fmt:message key="index.previous" /></span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#heroSlider" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden"><fmt:message key="index.next" /></span>
    </button>
</div>
<section class="content">

<div class="container mt-5">
    <h2 class="text-center"><fmt:message key="index.new_product" /></h2>

    <!-- Product Slider -->
    <div class="product-slider">
        <!-- Product List Container -->
        <div class="row row-cols-1 row-cols-md-4 g-4 mt-4">
            <!-- Placeholder for 10 products (you can dynamically load them via JSP/Servlet) -->
            <!-- Lặp qua danh sách sản phẩm mới nhất -->
            <c:forEach var="product" items="${newestProduct}">
                <div class="col-md-2 product-item">
                    <div class="card">
                        <img src="${pageContext.request.contextPath}/img/${product.imagePath}" class="card-img-top" alt="${product.name}" width="480">
                        <div class="card-body">
                            <h5 class="card-title">${product.name}</h5>
                            <p class="card-text">
                                <fmt:formatNumber value="${product.price - (product.price * (product.discountPercentage / 100))}"  minFractionDigits="0" maxFractionDigits="0"/>
                                <span class="currency">VND</span>
                            </p>
                            <p class="card-text text-muted" style="text-decoration: line-through; font-size: 0.8em;">
                                <fmt:formatNumber value="${product.price}" minFractionDigits="0" maxFractionDigits="0"/>
                                <span class="currency">VND</span>
                            </p>
                            <!-- Giảm giá phần trăm với mũi tên đi xuống -->
                            <div class="discount-badge" style="position: absolute; top: 10px; right: 10px; background-color: #ff6f61; color: white; padding: 5px 10px; border-radius: 5px; display: flex; align-items: center;">
                                <span>${product.discountPercentage}%</span>
                                <span style="margin-left: 5px; font-size: 10px; transform: rotate(45deg);">&#x2193;</span> <!-- Mũi tên đi xuống -->
                            </div>
                            <a href="productdetail?id=${product.productId}" class="btn btn-primary"><fmt:message key="index.view_detail" /></a>
                        </div>
                    </div>
                </div>
            </c:forEach>

        </div>

        <!-- Navigation Buttons -->
        <div class="text-center mt-4">
            <button id="prevBtn" class="btn btn-secondary" onclick="changeSlide(-1)">
                <i class="fas fa-arrow-left"></i> <fmt:message key="index.previous" />
            </button>
            <button id="nextBtn" class="btn btn-secondary" onclick="changeSlide(1)">
                <fmt:message key="index.next" /> <i class="fas fa-arrow-right"></i>
            </button>
        </div>
    </div>
</div>
</section>
<!-- Footer -->
<jsp:include page="includes/footer.jsp" />

<!-- Thêm tệp JavaScript trong trang index.html -->
<script>
    // JavaScript to control the product slider
    let currentIndex = 0;
    const productsPerSlide = 6; // Number of products shown per slide
    const products = document.querySelectorAll('.product-item'); // All product items
    const totalProducts = products.length; // Total number of products

    // Initially hide all products
    function hideAllProducts() {
        products.forEach(product => {
            product.style.display = 'none';
        });
    }

    // Show products for the current slide
    function showCurrentSlide() {
        hideAllProducts(); // Hide all products first
        // Show products for the current slide
        for (let i = currentIndex; i < currentIndex + productsPerSlide && i < totalProducts; i++) {
            products[i].style.display = 'block';
        }
    }

    // Move to the previous/next slide
    function changeSlide(direction) {
        currentIndex += direction * productsPerSlide; // Adjust current index based on direction

        // If reached the beginning, loop back to the last set of products
        if (currentIndex < 0) {
            currentIndex = totalProducts - productsPerSlide;
        }

        // If reached the end, loop back to the first set of products
        if (currentIndex >= totalProducts) {
            currentIndex = 0;
        }

        showCurrentSlide(); // Show products for the new current slide
    }

    // Initial call to show the first slide
    showCurrentSlide();

</script>
<script src="app.js"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist"/>
