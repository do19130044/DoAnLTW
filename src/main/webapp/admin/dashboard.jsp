
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard - Quản lý Người Dùng</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <style>
    .centered-container {
    display: flex;
    justify-content: center; /* Căn giữa theo chiều ngang */
    align-items: center;     /* Căn giữa theo chiều dọc */
    height: 100vh;/* Chiều cao của container sẽ là 100% chiều cao viewport */
    margin-bottom: 200px;
  }</style>
</head>
<body>
<jsp:include page="admin_header.jsp" />

<div class="container mt-5">
  <h2>Dashboard - Thống kê năm ${curYear}</h2>
  <div class="dropdown">
    <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
      Chọn năm
    </button>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
      <!-- Listbox items -->

        <c:forEach var="year" items="${years}">
          <li><a class="dropdown-item" href="dashboard?year=${year}">${year}</a></li>
        </c:forEach>
    </ul>
  </div>



  <!-- Biểu đồ Doanh Thu Theo Tháng -->
  <h3 style="text-align: center; margin-bottom: 100px">Biểu đồ Doanh Thu Theo Tháng</h3>
  <div class="centered-container">
  <div style="width: 800px; height: 800px">
  <canvas id="revenueChart"></canvas>
  </div>
  </div>
  <script>
    // Dữ liệu doanh thu theo tháng, được truyền từ servlet vào JSP dưới dạng mảng
        // Chuyển mảng BigDecimal sang mảng số thực trong JavaScript
    var monthlyRevenue = [];
    <c:forEach var="revenue" items="${monthlyRevenue}">
    monthlyRevenue.push(${revenue});
    </c:forEach>
    // console.log(monthlyRevenue);  // Kiểm tra dữ liệu trong console


    // Dữ liệu cho biểu đồ
    var data = {
      labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
        "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
      datasets: [{
        label: 'Doanh Thu Theo Tháng',
        data: monthlyRevenue,  // Dữ liệu doanh thu theo tháng
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(153, 102, 255, 0.2)',
          'rgba(255, 159, 64, 0.2)',
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(153, 102, 255, 0.2)',
          'rgba(255, 159, 64, 0.2)'
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
          'rgba(153, 102, 255, 1)',
          'rgba(255, 159, 64, 1)',
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
          'rgba(153, 102, 255, 1)',
          'rgba(255, 159, 64, 1)'
        ],
        borderWidth: 1
      }]
    };

    var ctx = document.getElementById('revenueChart').getContext('2d');
    var revenueChart = new Chart(ctx, {
      type: 'pie',  // Loại biểu đồ là biểu đồ tròn
      data: data,
      options: {
        responsive: true
      }
    });

  </script>

  <!-- Thống kê người dùng, doanh thu và đơn hàng đã hoàn thành -->
  <div class="row mt-4">
    <div class="col-md-4">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title">Tổng số người dùng</h5>
          <p class="card-text">${userCount}</p>
        </div>
      </div>
    </div>

    <div class="col-md-4">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title">Tổng doanh thu</h5>
          <p class="card-text">${totalRevenue}</p>
        </div>
      </div>
    </div>

    <div class="col-md-4">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title">Đơn hàng đã hoàn thành</h5>
          <p class="card-text">${completedOrders}</p>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="admin_footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
