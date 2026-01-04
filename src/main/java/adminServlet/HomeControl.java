package adminServlet;

import adminDao.ProductDao;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/home")
public class HomeControl extends HttpServlet {
    private static final int PAGE_SIZE = 10; // Số sản phẩm mỗi trang

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1; // Mặc định trang đầu tiên
        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }

        ProductDao productDao = new ProductDao();
        try {
            // Lấy danh sách sản phẩm với phân trang
            List<Product> products = productDao.getAllProducts(page, PAGE_SIZE);

            // Lấy tổng số sản phẩm để tính số trang
            int totalProducts = productDao.getTotalProducts();
            int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);

            // Chuyển dữ liệu vào request
            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

