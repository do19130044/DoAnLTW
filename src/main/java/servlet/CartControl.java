package servlet;

import dao.CartDao;
import model.Cart;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/cart")
public class CartControl extends HttpServlet {
    private CartDao cartDao = new CartDao();
    // Hiển thị giỏ hàng
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user"); // Lấy thông tin người dùng từ session

        if (user != null) {
            // Lấy giỏ hàng của người dùng
            Cart cart = cartDao.getCartByUserId(user.getId());
            session.setAttribute("cart", cart);

            // Tính lại tổng số sản phẩm trong giỏ và lưu vào session
            int totalItems = cartDao.getTotalItems(cart.getId());
            session.setAttribute("totalItems", totalItems);
            request.setAttribute("cart", cart); // Đặt giỏ hàng vào attribute của request
            RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
            dispatcher.forward(request, response);
//            response.sendRedirect("cart");
        } else {
            // Nếu người dùng chưa đăng nhập, yêu cầu đăng nhập
            response.sendRedirect("login.jsp");
        }
    }

    // Xử lý hành động xóa sản phẩm trong giỏ
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");


        if ("remove".equals(action)) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                Cart cart = cartDao.getCartByUserId(user.getId());
                if (cartDao.removeFromCart(cart.getId(), productId)) {
                    response.sendRedirect("cart");
                } else {
                    response.sendRedirect("cart.jsp?error=removeFailed");
                }
            } else {
                response.sendRedirect("login.jsp");
            }
        }
    }
}
