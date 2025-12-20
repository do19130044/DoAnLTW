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

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    // Xử lý yêu cầu GET để hiển thị form nhập mật khẩu mới
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        //messgese bundle
        HttpSession session = request.getSession(false);
        String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
        }
        // Đặt locale theo ngôn ngữ người dùng chọn
        Locale locale = new Locale(lang);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        String resetpassErr = messages.getString("reset-pass.error");


        // Kiểm tra token hợp lệ trong cơ sở dữ liệu
        if (token == null || token.isEmpty()) {
            request.setAttribute("error", resetpassErr);
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }

        // Tìm người dùng dựa trên token
        User user = userDao.findByResetToken(token);

        if (user != null) {
            // Token hợp lệ, hiển thị form nhập mật khẩu mới
            request.setAttribute("token", token);  // Gửi token cho form
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
        } else {
            // Token không hợp lệ hoặc hết hạn
            request.setAttribute("error", resetpassErr);
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
        }
    }

    // Xử lý yêu cầu POST khi người dùng nhập mật khẩu mới
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("password");

        //messgese bundle
        HttpSession session = request.getSession(false);
        String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
        }
        // Đặt locale theo ngôn ngữ người dùng chọn
        Locale locale = new Locale(lang);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        String resetpassErr = messages.getString("reset-pass.error");
        String resetpassSuc = messages.getString("reset-pass.success");


        // Kiểm tra token hợp lệ trong cơ sở dữ liệu
        if (token == null || token.isEmpty()) {
            request.setAttribute("error", resetpassErr);
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }

        // Tìm người dùng dựa trên token
        User user = userDao.findByResetToken(token);

        if (user != null) {
            // Cập nhật mật khẩu mới
            userDao.updateFPassword(user.getId(), BCrypt.hashpw(newPassword, BCrypt.gensalt()));

            // Thông báo thành công
            request.setAttribute("message", resetpassSuc);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            // Token không hợp lệ hoặc hết hạn
            request.setAttribute("error", resetpassErr);
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
        }
    }
}

