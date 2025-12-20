package servlet;

import dao.UserDao;
import mail.SendEmailUtil;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng tới trang yêu cầu email
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        //messgese bundle
        HttpSession session = request.getSession(false);
        String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
        }
        // Đặt locale theo ngôn ngữ người dùng chọn
        Locale locale = new Locale(lang);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        String resetpassErrUser = messages.getString("forgot-pass.error-user");
        String resetpassErrEmail = messages.getString("forgot-pass.error-email");
        String resetpassMess = messages.getString("forgot-pass.message");

        // Tìm người dùng theo email
        User user = userDao.findByEmail(email);

        if (user != null) {
            // Tạo token ngẫu nhiên
            String resetToken = UUID.randomUUID().toString();

            // Thời gian hết hạn của token (ví dụ là 1 giờ)
            Date expiryDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000); // 1 giờ

            // Lưu token và thời gian hết hạn vào cơ sở dữ liệu
            userDao.saveResetToken(user.getId(), resetToken, expiryDate);

            // Tạo liên kết reset mật khẩu
            String resetLink = "http://localhost:8080/ESC_war_exploded/reset-password?token=" + resetToken;

            // Gửi email với token cho người dùng
            try {
                SendEmailUtil.sendPasswordResetEmail(user.getEmail(), resetLink);
            } catch (IOException e) {
                e.printStackTrace();
                request.setAttribute("error", resetpassErrUser);
                request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
                return;
            }

            // Thông báo thành công
            request.setAttribute("message", resetpassMess);
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
        } else {
            // Người dùng không tồn tại
            request.setAttribute("error", resetpassErrEmail);
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
        }
    }
}
