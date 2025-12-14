package servlet;

import dao.UserDao;
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

@WebServlet("/register")

public class RegisterControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chỉ đơn giản tải trang register.jsp
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Vai trò mặc định là CUSTOMER
        Role role = Role.CUSTOMER;
        //messgese bundle
        HttpSession session = request.getSession(false);
        String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
        }
        // Đặt locale theo ngôn ngữ người dùng chọn
        Locale locale = new Locale(lang);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

        // Kiểm tra người dùng đã tồn tại chưa
        UserDao userDao = new UserDao();
        if (userDao.checkUserExists(username, email)) {
            String registerMess = messages.getString("register.exist");
            request.setAttribute("error", registerMess);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else {
            // Hash mật khẩu
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            // Lưu người dùng mới
            User user = new User(username, hashedPassword, email, phone, role);
            if (userDao.saveUser(user)) {
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                String registerMess = messages.getString("register.error");
                request.setAttribute("error", registerMess);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        }
    }

}
