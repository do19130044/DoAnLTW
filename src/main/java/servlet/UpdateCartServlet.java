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
@WebServlet("/update")
public class UpdateCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    CartDao cartDao = new CartDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin sản phẩm và số lượng từ request
        // Lấy thông tin từ form
        int cartId = Integer.parseInt(request.getParameter("cartId"));         //ID cua cart
        int productId = Integer.parseInt(request.getParameter("productId"));  // ID sản phẩm
        int newQuantity = Integer.parseInt(request.getParameter("quantity")); // Số lượng mới

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        // Lấy thông tin người dùng từ session
        if (user != null) {
            // Nếu người dùng đã đăng nhập, thực hiện cập nhật giỏ hàng
            boolean success = cartDao.updateCartItemQuantity(cartId, productId, newQuantity);
            if (success) {
                // Cập nhật lại giỏ hàng và số lượng sản phẩm trong giỏ hàng
                Cart cart = cartDao.getCartByUserId(user.getId());
                session.setAttribute("cart", cart);
                int totalItems = cartDao.getTotalItems(cart.getId());
                session.setAttribute("totalItems", totalItems);
                // Nếu cập nhật thành công, chuyển hướng về trang giỏ hàng
                response.sendRedirect("cart"); // Giả sử giỏ hàng có URL là /cart
            } else {
                String lang = (String) session.getAttribute("lang");
                if (lang == null) {
                    lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
                }
                // Đặt locale theo ngôn ngữ người dùng chọn
                Locale locale = new Locale(lang);
                ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
                String updateErr = messages.getString("cart.update-err");

                // Nếu có lỗi trong quá trình cập nhật
                request.setAttribute("error", updateErr);
                request.getRequestDispatcher("cart.jsp").forward(request, response);
            }
        } else {
            // Nếu người dùng chưa đăng nhập, yêu cầu đăng nhập
            response.sendRedirect("login"); // Redirect đến trang đăng nhập
        }
    }
}
