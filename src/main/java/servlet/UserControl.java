package servlet;

import dao.UserDao;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/user")

public class UserControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin người dùng từ session (giả sử người dùng đã đăng nhập)
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (user != null) {
            // Lấy thêm thông tin từ CSDL nếu cần thiết (ví dụ: nếu thông tin chưa đầy đủ trong session)
            // Nếu thông tin đã đầy đủ trong session, có thể bỏ qua bước này.

            // Ví dụ: Lấy thông tin từ CSDL bằng JDBC (thực hiện lấy lại thông tin từ cơ sở dữ liệu)
            // Giả sử bạn có một UserDAO để lấy thông tin người dùng từ DB
            UserDao userDao = new UserDao();
            user = userDao.getUserById(user.getId());

            // Đặt lại thông tin người dùng vào session
            session.setAttribute("user", user);

            // Chuyển hướng tới trang user.jsp để hiển thị thông tin người dùng
            RequestDispatcher dispatcher = request.getRequestDispatcher("/user.jsp");
            dispatcher.forward(request, response);
        } else {
            // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang login
            response.sendRedirect(request.getContextPath()+"/login");
        }
    }
    @Override
    // POST method để cập nhật thông tin người dùng
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy ngôn ngữ từ application scope
        HttpSession session = request.getSession(false);
        String lang = (String) session.getAttribute("lang");
        if (lang == null) {
            lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
        }
        // Đặt locale theo ngôn ngữ người dùng chọn
        Locale locale = new Locale(lang);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);


        // Lấy thông tin người dùng từ form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String id = request.getParameter("userId");
        int userId;
        if(id==null){
            User user = (User) request.getSession().getAttribute("user");
             userId = user.getId();

        }else {
             userId = Integer.parseInt(id);
        }

        // Tạo đối tượng User và set thông tin
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);

        // Cập nhật thông tin người dùng trong DB
        UserDao userDao = new UserDao();
        boolean isUpdated = userDao.updateUser(user); // gọi phương thức updateUser trong UserDao

        if (isUpdated) {
            // Cập nhật lại thông tin trong session
            session.setAttribute("user", user);

            // Sau khi cập nhật thành công, chuyển hướng về trang profile
            response.sendRedirect(request.getContextPath() +"/user");
        } else {
            String updateMess = messages.getString("update.error");
            // Nếu cập nhật thất bại, quay lại trang profile với thông báo lỗi
            request.setAttribute("error", updateMess);
            request.getRequestDispatcher("user.jsp").forward(request, response);
        }
    }
}
