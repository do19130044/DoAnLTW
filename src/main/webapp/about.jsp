<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>About Us - Tech Shop</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>

<!-- Header -->
<jsp:include page="includes/header.jsp" />
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />

<!-- About Us Section -->
<section class="container mt-5">
  <h1 class="text-center">About Us</h1>
  <div class="row">
    <div class="col-md-6">
      <h3>Our Mission</h3>
      <p><fmt:message key="about.our_mission"/> </p>
    </div>
    <div class="col-md-6">
      <h3>Our Values</h3>
      <p><fmt:message key="about.our_value"/>/p>
    </div>
  </div>

  <div class="row mt-5">
    <div class="col-md-6">
      <h3>Contact Us</h3>
      <p><fmt:message key="about.our_contact"/> </p>
    </div>
    <div class="col-md-6">
      <h3>Our Team</h3>
      <p><fmt:message key="about.our_team"/></p>
    </div>
  </div>

  <div class="row mt-5">
    <div class="col-md-12">
      <h3>About Info</h3>
      <p><fmt:message key="about.our_info" /></p>
    </div>
  </div>
</section>

<!-- Footer -->
<jsp:include page="includes/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="app.js"></script>
</body>

</html>
