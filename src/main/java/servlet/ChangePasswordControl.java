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

@WebServlet("/user_change_password")

public class ChangePasswordControl extends HttpServlet {


        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy ngôn ngữ từ application scope
            HttpSession session= request.getSession(false);
            String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
        }
        // Đặt locale theo ngôn ngữ người dùng chọn
        Locale locale = new Locale(lang);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

        String oldPassword = request.getParameter("oldPassword");

        // Lấy mật khẩu hiện tại từ cơ sở dữ liệu
        // Lấy đối tượng User từ session
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath()+"/login"); // Nếu người dùng chưa đăng nhập
            return;
        }

        int userId = user.getId(); // Lấy userId từ đối tượng User

        // Kiểm tra mật khẩu cũ
        UserDao userDao = new UserDao();
        User dbUser = userDao.getUserById(userId);

        if (BCrypt.checkpw(oldPassword, dbUser.getPassword())) {
            // Mật khẩu cũ đúng, chuyển hướng đến trang nhập mật khẩu mới
            response.sendRedirect(request.getContextPath()+ "/user_new_password");
        } else {
            String changePassMess = messages.getString("changePass.error");

            // Nếu mật khẩu cũ sai, hiển thị thông báo lỗi
            request.setAttribute("error", changePassMess);
            request.getRequestDispatcher("/user.jsp").forward(request, response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
