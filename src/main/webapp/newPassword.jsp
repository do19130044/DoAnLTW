<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>New Password - Tech Shop</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"></head>

<body>
<jsp:include page="includes/header.jsp" />
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />
<section class="content">
<div class="container mt-5">
  <h2 class="text-center"><fmt:message key="new_pass.title" /></h2>
  <c:if test="${not empty error}">
    <p class="text-center text-danger">${error}</p>
  </c:if>

  <!-- Center the form -->
  <div class="row justify-content-center">
    <div class="col-md-6">
      <form id="newPasswordForm" action="user_new_password" method="post" onsubmit="return validatePasswords()">
        <div class="mb-3">
          <label for="newPassword" class="form-label"><fmt:message key="new_pass.new_pass" /></label>
          <input type="password" class="form-control" id="newPassword" name="newPassword" required>
        </div>
        <div class="mb-3">
          <label for="confirmNewPassword" class="form-label"><fmt:message key="new_pass.confirm" /></label>
          <input type="password" class="form-control" id="confirmNewPassword" required>
          <div id="passwordError" class="text-danger mt-2" style="display:none;"><fmt:message key="new_pass.match_error" /></div>
        </div>
        <button type="submit" class="btn btn-primary w-100"><fmt:message key="new_pass.submit" /></button>
      </form>
    </div>
  </div>
</div>
</section>
<!-- Footer -->
<jsp:include page="includes/footer.jsp" />
<script>
  function validatePasswords() {
    var newPassword = document.getElementById("newPassword").value;
    var confirmNewPassword = document.getElementById("confirmNewPassword").value;

    if (newPassword !== confirmNewPassword) {
      document.getElementById("passwordError").style.display = "block";
      return false;
    }
    return true;
  }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="app.js"></script>
</body>

</html>


