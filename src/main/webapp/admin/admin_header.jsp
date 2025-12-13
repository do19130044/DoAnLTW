
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<header>
    <!-- Navbar for Admin -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light shadow-sm">
        <div class="container-fluid">
            <!-- Logo -->
            <a class="navbar-brand" href="home">
                <i class="fas fa-cogs"></i> ESC admin
            </a>
            <!-- Toggle button for mobile view -->
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAdminNav"
                    aria-controls="navbarAdminNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <!-- Navbar items for Admin -->
            <div class="collapse navbar-collapse" id="navbarAdminNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="dashboard?year=">
                            <i class="fas fa-tachometer-alt"></i> Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="home">
                            <i class="fas fa-box"></i> Manage Products
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="order-list">
                            <i class="fas fa-box"></i> Orders
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="user-detail">
                            <i class="fas fa-users"></i> User Management
                        </a>
                    </li>
                    <c:if test="${not empty sessionScope.user}">
                        <li class="nav-item">
                            <a class="nav-link" href="#">
                                <i class="fas fa-users"></i> Xin Chào, ${sessionScope.user.username}
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="logout">
                                <i class="fas fa-users"></i>Logout
                            </a>
                        </li>

                    </c:if>
                </ul>
            </div>
        </div>
    </nav>
</header>

