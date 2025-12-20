package servlet;

import dao.ProductDao;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")

public class HomeControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chỉ đơn giản tải trang index.jsp
        ProductDao productDao = new ProductDao();
        List<Product> newestProduct = productDao.getNewestProduct();

        request.setAttribute("newestProduct", newestProduct);
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }

}
