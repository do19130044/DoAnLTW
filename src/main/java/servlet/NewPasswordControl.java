package servlet;

import dao.UserDao;
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

@WebServlet("/user_new_password")

public class NewPasswordControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chỉ đơn giản tải trang .jsp
        // Lấy đối tượng User từ session
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath()+"/login"); // Nếu người dùng chưa đăng nhập
            return;
        }
        request.getRequestDispatcher("newPassword.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newPassword = request.getParameter("newPassword");

        // Mã hóa mật khẩu mới
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Lấy đối tượng User từ session
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath()+ "/login"); // Nếu người dùng chưa đăng nhập
            return;
        }

        int userId = user.getId(); // Lấy userId từ đối tượng User

        // Cập nhật mật khẩu trong cơ sở dữ liệu
        UserDao userDAO = new UserDao();
        boolean isUpdated = userDAO.updatePassword(userId, hashedPassword);
        HttpSession session = request.getSession(false);
        String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
        }
        // Đặt locale theo ngôn ngữ người dùng chọn
        Locale locale = new Locale(lang);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

        if (isUpdated) {
            // Cập nhật thành công, chuyển hướng về trang profile
            user.setPassword(hashedPassword);  // Cập nhật mật khẩu trong session nếu thành công
            response.sendRedirect(request.getContextPath()+ "/user");
        } else {
            // Nếu có lỗi, hiển thị thông báo lỗi
            String newpassErr = messages.getString("new_pass.error");
            request.setAttribute("error", newpassErr);
            request.getRequestDispatcher("newPassword.jsp").forward(request, response);
        }
    }
}
