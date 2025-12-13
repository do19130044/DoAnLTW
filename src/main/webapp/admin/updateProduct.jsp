<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cập nhật sản phẩm</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h2>Cập nhật sản phẩm</h2>
    <form  action="updateProduct" method="post"  enctype="multipart/form-data" >
        <input type="hidden" name="productId" value="${product.productId}" />
        <div class="form-group">
            <label for="productName">Tên sản phẩm</label>
            <input type="text" class="form-control" id="productName" name="name" value="${product.name}" required>
        </div>
        <div class="form-group">
            <label for="productDescription">Mô tả</label>
            <textarea class="form-control" id="productDescription" name="description" required>${product.description}</textarea>
        </div>
        <div class="form-group">
            <label for="productPrice">Giá</label>
            <input type="number" class="form-control" id="productPrice" name="price" value="${product.price}" required>
        </div>
        <div class="form-group">
            <label for="productStock">Số lượng</label>
            <input type="number" class="form-control" id="productStock" name="stock" value="${product.stock}" required>
        </div>
        <div class="form-group">
            <label for="productCategory">Danh mục</label>
            <input type="text" class="form-control" id="productCategory" name="category" value="${product.category}" required>
        </div>
        <div class="form-group">
            <label for="productDiscount">Giảm giá (%)</label>
            <input type="number" class="form-control" id="productDiscount" name="discountPercentage" value="${product.discountPercentage}" required>
        </div>
        <div class="form-group">
            <label for="productImage">Ảnh sản phẩm</label>
            <input type="file" class="form-control" id="productImage" name="image">
            <input type="hidden" name="oldImg" value="${product.imagePath}">
        </div>
        <button type="submit" class="btn btn-primary">Cập nhật</button>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>

