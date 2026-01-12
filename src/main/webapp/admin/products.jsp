<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
    <fmt:setBundle basename="messages" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Sản phẩm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <link rel="stylesheet" href="style.css">
</head>
<body>
  <jsp:include page="admin_header.jsp" />
  <section class="content">
<div class="container mt-5">






    <!-- Bảng sản phẩm -->
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Tên sản phẩm</th>
            <th>Mô tả</th>
            <th>Giá</th>
            <th>Số lượng trong kho</th>
            <th>Danh mục</th>
            <th>Hình ảnh</th>
            <th>Ngày tạo</th>
            <th>Giảm giá (%)</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>${product.name}</td>
                <td>${product.description}</td>
                <td>${product.price}</td>
                <td>${product.stock}</td>
                <td>${product.category}</td>
                <td>
                    <img src="${pageContext.request.contextPath}/img/${product.imagePath}" alt="image" width="100" loading="lazy">
                </td>
                <td>${product.createdAt}</td>
                <td>${product.discountPercentage}</td>
                <td>
                    <button class="btn btn-warning" onclick="editProduct(${product.productId}, '${product.name}', ${product.price} ,
                            '${product.description}', '${product.category}', ${product.discountPercentage},
                            ${product.stock}, '${product.imagePath}' )">Sửa</button>
                </td>
                <!--nút xóa-->
                <td>
                    <button type="button"
                            class="btn btn-danger btn-sm"
                            onclick="deleteProduct(${product.productId})">
                        Xóa
                    </button>


                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- Phân trang -->
    <nav>
        <ul class="pagination">
            <c:if test="${currentPage > 1}">
                <li class="page-item"><a class="page-link" href="?page=${currentPage - 1}">Trang trước</a></li>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="?page=${i}">${i}</a>
                </li>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <li class="page-item"><a class="page-link" href="?page=${currentPage + 1}">Trang sau</a></li>
            </c:if>
        </ul>
    </nav>

    <!-- Nút Thêm -->
    <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addProductModal">Thêm sản phẩm</button>
    <c:if test="${not empty message}">
        <p style="color: red;">${message}</p>
    </c:if>


</div>

      <!-- Modal Thêm Sản Phẩm -->
      <div class="modal fade" id="addProductModal" tabindex="-1" aria-labelledby="addProductModalLabel" aria-hidden="true">
          <div class="modal-dialog">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title" id="addProductModalLabel">Thêm Sản Phẩm</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                      <form id="addProductForm" enctype="multipart/form-data">
                          <div class="mb-3">
                              <label for="productName" class="form-label">Tên sản phẩm</label>
                              <input type="text" class="form-control" id="productName" name="name" required>
                          </div>
                          <div class="mb-3">
                              <label for="productPrice" class="form-label">Giá</label>
                              <input type="number" class="form-control" id="productPrice" name="price" value="1" min="1"required>
                          </div>
                          <div class="mb-3">
                              <label for="productDescription" class="form-label">Mô tả</label>
                              <textarea class="form-control" id="productDescription" name="description" required></textarea>
                          </div>
                          <%--                    <div class="mb-3">--%>
                          <%--                        <label for="productCategory" class="form-label">Danh mục</label>--%>
                          <%--                        <input type="text" class="form-control" id="productCategory" name="category" required>--%>
                          <%--                    </div>--%>
                          <div class="mb-3">
                              <label for="productCategory" class="form-label">Danh mục</label>
                              <select class="form-control" id="productCategory" name="category" required>
                                  <option value="Laptop">Laptop</option>
                                  <option value="Phone">Phone</option>
                                  <option value="Tablet">Tablet</option>
                              </select>
                          </div>
                          <div class="mb-3">
                              <label for="productImage" class="form-label">Ảnh sản phẩm</label>
                              <input type="file" class="form-control" id="productImage" name="image" required>
                          </div>
                          <div class="mb-3">
                              <label for="discountPercentage" class="form-label">Giảm giá (%)</label>
                              <input type="number" step="0.01" class="form-control" id="discountPercentage" name="discountPercentage"value="1" min="1"required>
                          </div>
                          <div class="mb-3">
                              <label for="stock" class="form-label">Số lượng</label>
                              <input type="number" class="form-control" id="stock" name="stock" value="1" min="1"required>
                          </div>
                      </form>
                  </div>
                  <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                      <button type="button" class="btn btn-primary" onclick="saveProduct()" >Lưu</button>
                  </div>
              </div>
          </div>
      </div>

      <!-- Modal Sửa Sản Phẩm -->
<div class="modal fade" id="editProductModal" tabindex="-1" aria-labelledby="editProductModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editProductModalLabel">Sửa Sản Phẩm</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="editProductForm" enctype="multipart/form-data">
                    <input type="hidden" id="editProductId" name="productId" > <!-- Ẩn id sản phẩm để gửi lên server -->

                    <div class="mb-3">
                        <label for="editProductName" class="form-label">Tên sản phẩm</label>
                        <input type="text" class="form-control" id="editProductName" name="name">

                    </div>

                    <div class="mb-3">
                        <label for="editProductPrice" class="form-label">Giá</label>
                        <input type="number" class="form-control" id="editProductPrice" name="price" value="1" min="1">
                    </div>

                    <div class="mb-3">
                        <label for="editProductDescription" class="form-label">Mô tả</label>
                        <textarea class="form-control" id="editProductDescription" name="description"></textarea>
                    </div>

                    <div class="mb-3">
                        <label for="editProductCategory" class="form-label">Danh mục</label>
                        <select class="form-control" id="editProductCategory" name="category" required>
                            <option value="Laptop">Laptop</option>
                            <option value="Phone">Phone</option>
                            <option value="Tablet">Tablet</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="editProductImage" class="form-label">Ảnh sản phẩm (để trống nếu không thay đổi)</label>
                        <input type="file" class="form-control" id="editProductImage" name="image">
                            <input type="hidden" id="editOldProductImage" name="oldImg ">
                    </div>

                    <div class="mb-3">
                        <label for="editDiscountPercentage" class="form-label">Giảm giá (%)</label>
                        <input type="number" class="form-control" id="editDiscountPercentage" name="discountPercentage" value="1" min="1" step="0.01">
                    </div>

                    <div class="mb-3">
                        <label for="editProductStock" class="form-label">Số lượng</label>
                        <input type="number" class="form-control" id="editProductStock" name="stock" value="1" min="1">
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                <button type="button" class="btn btn-primary" onclick="updateProduct()">Cập nhật</button>
            </div>
        </div>
    </div>
</div>
  </section>

  <jsp:include page="admin_footer.jsp" />


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function validateForm() {
        const productName = document.getElementById('productName').value.trim();
        const productPrice = document.getElementById('productPrice').value.trim();
        const productDescription = document.getElementById('productDescription').value.trim();
        const productCategory = document.getElementById('productCategory').value.trim();
        const productImage = document.getElementById('productImage').value.trim();
        const discountPercentage = document.getElementById('discountPercentage').value.trim();
        const stock = document.getElementById('stock').value.trim();

        let isValid = true;
        let errorMessage = '';

        if (!productName) {
            errorMessage += "Tên sản phẩm không được để trống.\n";
            isValid = false;
        }
        if (!productPrice || parseFloat(productPrice) <= 0) {
            errorMessage += "Giá sản phẩm phải lớn hơn 0.\n";
            isValid = false;
        }
        if (!productDescription) {
            errorMessage += "Mô tả không được để trống.\n";
            isValid = false;
        }
        if (!productCategory) {
            errorMessage += "Danh mục không được để trống.\n";
            isValid = false;
        }
        if (!productImage) {
            errorMessage += "Ảnh sản phẩm không được để trống.\n";
            isValid = false;
        }
        if (!discountPercentage || parseFloat(discountPercentage) < 0) {
            errorMessage += "Giảm giá phải lớn hơn hoặc bằng 0.\n";
            isValid = false;
        }
        if (!stock || parseInt(stock) <= 0) {
            errorMessage += "Số lượng phải lớn hơn 0.\n";
            isValid = false;
        }

        if (!isValid) {
            alert(errorMessage);
        }
        return isValid;
    }


    function validateForm2() {
        const productName = document.getElementById('productName').value.trim();
        const productPrice = document.getElementById('productPrice').value.trim();
        const productDescription = document.getElementById('productDescription').value.trim();
        const productCategory = document.getElementById('productCategory').value.trim();
        const productImage = document.getElementById('productImage').value.trim();
        const discountPercentage = document.getElementById('discountPercentage').value.trim();
        const stock = document.getElementById('stock').value.trim();

        let isValid = true;
        let errorMessage = '';


        if (!productPrice || parseFloat(productPrice) <= 0) {
            errorMessage += "Giá sản phẩm phải lớn hơn 0.\n";
            isValid = false;
        }

        if (!productCategory) {
            errorMessage += "Danh mục không được để trống.\n";
            isValid = false;
        }

        if (!discountPercentage || parseFloat(discountPercentage) < 0) {
            errorMessage += "Giảm giá phải lớn hơn hoặc bằng 0.\n";
            isValid = false;
        }
        if (!stock || parseInt(stock) <= 0) {
            errorMessage += "Số lượng phải lớn hơn 0.\n";
            isValid = false;
        }

        if (!isValid) {
            alert(errorMessage);
        }
        return isValid;
    }


    //  Hàm lưu sản phẩm mới (thêm sản phẩm)
    function saveProduct() {
        if (validateForm()) {
            // Nếu form hợp lệ, gửi form đi
            const form = document.getElementById('addProductForm');
            const formData = new FormData(form);

            // Gửi dữ liệu form qua AJAX
            fetch('addProduct', {
                method: 'POST',
                body: formData
            })
                .then(response => response.text())  // Đọc phản hồi từ server dưới dạng text
                .then(responseText => {
                    alert(responseText); // Hiển thị thông báo từ server
                    if (responseText.includes("<fmt:message key="add.success" />")) {
                        window.location.reload(); // Reload trang sản phẩm nếu thành công
                    }
                })
                .catch(error => {
                    console.error('Lỗi:', error);
                    alert('Có lỗi xảy ra');
                });
        }

    }

   // Hàm mở modal sửa sản phẩm
    function editProduct(productId , name, price, description, category, discountPercentage, stock, imagePath) {
        // Điền thông tin sản phẩm vào form
        document.getElementById('editProductId').value = productId;
        document.getElementById('editProductName').value = name;
        document.getElementById('editProductPrice').value = price;
        document.getElementById('editProductDescription').value = description;
        document.getElementById('editProductCategory').value = category;
        document.getElementById('editDiscountPercentage').value = discountPercentage;
        document.getElementById('editProductStock').value = stock;
        document.getElementById('editOldProductImage').value = imagePath;

        // Chọn đúng danh mục trong dropdown
        const categorySelect = document.getElementById('editProductCategory');
        for (let option of categorySelect.options) {
            if (option.value === category) {
                option.selected = true;
                break;
            }
        }
        // Nếu có ảnh sản phẩm, ta có thể hiển thị ảnh này cho người dùng
        // Nếu bạn muốn hiển thị ảnh, cần phải cập nhật phần này

        const modal = new bootstrap.Modal(document.getElementById('editProductModal'));
        modal.show();
    }

    // Hàm cập nhật sản phẩm
    function updateProduct() {
        if (validateForm2()) {
            const form = document.getElementById('editProductForm');
            const formData = new FormData(form);

            // Lấy id của sản phẩm cần cập nhật
            const productId = document.getElementById('editProductId').value;

            // Thêm id vào formData để server biết cần cập nhật sản phẩm nào
            formData.append('productId', productId);

            // Gửi dữ liệu form qua AJAX
            fetch('updateProduct', {
                method: 'POST',
                body: formData
            })
                .then(response => response.text())
                .then(responseText => {
                    alert(responseText); // Hiển thị thông báo từ server
                    if (responseText.includes("<fmt:message key="update.success" />")) {
                        window.location.reload(); // Reload trang sản phẩm nếu thành công
                    }
                })
                .catch(error => {
                    console.error('Lỗi:', error);
                    alert('Có lỗi xảy ra');
                });
        }

    }

    // Hàm xóa sản phẩm
    function deleteProduct(id) {
        if (!confirm("Bạn có chắc chắn muốn xóa sản phẩm này không?")) return;

        fetch('${pageContext.request.contextPath}/admin/deleteProduct', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'id=' + id
        })
            .then(res => res.text())
            .then(msg => {
                alert(msg);
                location.reload();
            })
            .catch(err => {
                console.error(err);
                alert('Có lỗi xảy ra');
            });
    }



    // Tính năng tìm kiếm sản phẩm
    document.getElementById('search').addEventListener('input', function(event) {
        const searchText = event.target.value.toLowerCase();
        const rows = document.querySelectorAll('#product-table tr');
        rows.forEach(row => {
            const name = row.cells[1].textContent.toLowerCase();
            if (name.includes(searchText)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });
</script>
</body>
</html>

