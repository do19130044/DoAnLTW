package servlet;

import dao.CartDao;
import model.Cart;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/Addtocart")
public class AddToCartServlet extends HttpServlet {
    private CartDao cartDao = new CartDao();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin từ form
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user"); // Lấy thông tin người dùng từ session
        if (user != null) {
            // Nếu người dùng đã đăng nhập, thêm sản phẩm vào giỏ hàng
            boolean success = cartDao.addToCart(user.getId(), productId, quantity);
            if (success) {
                Cart cart = cartDao.getCartByUserId(user.getId());
                session.setAttribute("cart", cart);
                int totalItems = cartDao.getTotalItems(cart.getId());
                session.setAttribute("totalItems", totalItems);
                // Sau khi thêm sản phẩm, chuyển hướng về giỏ hàng
                response.sendRedirect("cart");
            } else {
                String lang = (String) session.getAttribute("lang");
                if (lang == null) {
                    lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
                }
                // Đặt locale theo ngôn ngữ người dùng chọn
                Locale locale = new Locale(lang);
                ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
                String addErr = messages.getString("cart.add-err");

                // Nếu có lỗi trong quá trình thêm sản phẩm
                request.setAttribute("error", addErr);
                request.getRequestDispatcher("product-detail.jsp").forward(request, response);
            }
        } else {
            // Nếu người dùng chưa đăng nhập, yêu cầu đăng nhập
            response.sendRedirect("login");
        }
    }
}
