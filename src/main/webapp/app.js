document.addEventListener('DOMContentLoaded', function () {
    // Lấy phần tử danh mục sản phẩm
    const productNav = document.querySelector('.navbar-nav');
    const dropdownMenus = document.querySelectorAll('.dropdown-menu');

    // Hiển thị menu khi hover vào mục sản phẩm
    productNav.addEventListener('mouseover', function (event) {
        if (event.target.closest('.dropdown')) {
            event.target.closest('.dropdown').querySelector('.dropdown-menu').classList.add('show');
        }
    });

    // Ẩn menu khi rời chuột khỏi mục sản phẩm
    productNav.addEventListener('mouseout', function (event) {
        if (event.target.closest('.dropdown')) {
            event.target.closest('.dropdown').querySelector('.dropdown-menu').classList.remove('show');
        }
    });
});
document.getElementById('loginBtn').addEventListener('click', function () {
    document.getElementById('loginSidebar').classList.add('active');
});

document.getElementById('registerBtn').addEventListener('click', function () {
    document.getElementById('registerSidebar').classList.add('active');
});

document.getElementById('closeLoginSidebar').addEventListener('click', function () {
    document.getElementById('loginSidebar').classList.remove('active');
});

document.getElementById('closeRegisterSidebar').addEventListener('click', function () {
    document.getElementById('registerSidebar').classList.remove('active');
});


