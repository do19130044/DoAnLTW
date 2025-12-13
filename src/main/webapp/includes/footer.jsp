<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" />
<fmt:setBundle basename="messages" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="bg-dark text-light mt-5 p-4 text-center">
  <p><fmt:message key="footer.content" /></p>
</footer>

