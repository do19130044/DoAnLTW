package servlet;
import dao.CartDao;
import dao.UserDao;
import model.Cart;
import model.Role;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/login")

public class LoginControl extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chỉ đơn giản tải trang login.jsp
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");


        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDao userDao = new UserDao();
        User user = userDao.getUserByUsername(username);
       // UserDAO userDAO = new UserDAO();
        //User user = userDAO.getUserByUsername(username);
      //  ResourceBundle messages = ResourceBundle.getBundle("messages", Locale.getDefault());
        // Lấy ngôn ngữ từ application scope
        HttpSession session = request.getSession(false);
        String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
        }
        // Đặt locale theo ngôn ngữ người dùng chọn
        Locale locale = new Locale(lang);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);


        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
           //  session = request.getSession();

            session.setAttribute("user", user);

            // Lấy giỏ hàng của người dùng và lưu vào session
            CartDao cartDao = new CartDao();
            Cart cart = cartDao.getCartByUserId(user.getId());
            session.setAttribute("cart", cart);

            // Tính tổng số lượng sản phẩm trong giỏ và lưu vào session
            int totalItems = cartDao.getTotalItems(cart.getId());
            session.setAttribute("totalItems", totalItems);
            // Kiểm tra nếu có URL yêu cầu được lưu trong session (trường hợp người dùng truy cập trang yêu cầu đăng nhập trước đó)
            String redirectUrl = (String) session.getAttribute("redirectUrl");
            //chuyển hướng đến trang trước khi đăng nhập
//        if (redirectUrl != null){
//            response.sendRedirect(response.encodeRedirectURL(redirectUrl));
//
//        }else {
//            // Phân quyền
            if (user.getRole().equals(Role.ADMIN)) {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+"/admin/home"));
            } else {
            //   request.getRequestDispatcher("/home").forward(request, response);
//                response.sendRedirect( "home");
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+"/home"));
            System.out.println(session.getAttribute("user"));
            }
    //    }
        } else {
            String loginMess = messages.getString("login.error");

            request.setAttribute("error", loginMess);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
