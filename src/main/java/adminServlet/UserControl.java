package adminServlet;


import adminDao.UserDao;
import model.Role;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/user-detail")
public class UserControl extends HttpServlet {
    private UserDao userDao = new UserDao();
    // Phương thức hiển thị danh sách người dùng
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = userDao.getUserById(userId);
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("edit-user.jsp").forward(request, response);
            } else {
                response.sendRedirect("user-list?error=User not found");
            }
        } else {
            List<User> users = userDao.getAllUser();
            request.setAttribute("users", users);
            request.getRequestDispatcher("user-list.jsp").forward(request, response);
        }
    }
    // Phương thức hiển thị chi tiết người dùng
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        // Kiểm tra session để xác thực xem người dùng đã đăng nhập chưa
        User admin = (User) request.getSession().getAttribute("user");
        if (admin == null) {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
            response.sendRedirect("login.jsp?error=Please login first");
            return; // Dừng lại ở đây nếu không có admin trong session
        }
        if ("view".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = userDao.getUserById(userId);
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("user-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("user-list?error=User not found");
            }
        } else if ("edit".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = userDao.getUserById(userId);
            if(user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("edit-user.jsp").forward(request, response);
                // Nếu không tìm thấy người dùng, hiển thị lỗi hoặc thông báo thích hợp
            } else {
                response.sendRedirect("user-list?error=User not found");
            }

        } else if ("delete".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            // Kiểm tra quyền của admin trước khi xóa
            if (admin.getRole() != Role.ADMIN) {
                response.sendRedirect("user-list?error=Unauthorized");
                return;
            }
            boolean isDeleted = userDao.deleteUser(admin, userId);
            if (isDeleted) {
                response.sendRedirect("user-detail");
            } else {
                response.sendRedirect("user-list?error=Delete failed");
            }

            // Kiểm tra quyền hạn của admin trước khi xóa
//            if (admin != null && admin.getRole() == Role.ADMIN) {
//                boolean isDeleted = userDao.deleteUser(admin, userId);
//                if (isDeleted) {
//                    response.sendRedirect("user-list");
//                } else {
//                    response.sendRedirect("user-list?error=Delete failed");
//
//                }
//            } else {
//                response.sendRedirect("user-list?error=Unauthorized");
//
//            }
        } else if ("update".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String role = request.getParameter("role");
            // Cập nhật thông tin người dùng
//            User admin = (User) request.getSession().getAttribute("admin");  // Lấy admin từ session
            // Nếu không có admin trong session, chuyển hướng về trang đăng nhập hoặc thông báo lỗi
//            if(admin == null) {
//            response.sendRedirect("login.jsp?error=Please login first");
//            return;  // Dừng lại ở đây nếu không có admin
//        }
//            if (admin.getRole() != Role.ADMIN) {
//                response.sendRedirect("user-list?error=Unauthorized");
//                return;
//            }
            // Nếu mật khẩu mới không được cung cấp, giữ mật khẩu cũ
            String password = request.getParameter("password");
            if (password == null || password.trim().isEmpty()) {
                // Giữ lại mật khẩu cũ, không thay đổi
                User existingUser = userDao.getUserById(userId);
                if (existingUser != null) {
                    password = existingUser.getPassword();  // Giữ mật khẩu cũ nếu không thay đổi
                } else {
                    // Nếu người dùng không tồn tại trong cơ sở dữ liệu, trả về lỗi
                    response.sendRedirect("edit-user?userId=" + userId + "&error=User not found");
                    return;
                }
            } else {
                // Nếu có mật khẩu mới, mã hóa và lưu trữ
                password = BCrypt.hashpw(password, BCrypt.gensalt());
            }
            User user = new User(userId, username, password, email, phone, Role.valueOf(role));
            boolean isUpdated = userDao.updateUser(admin, user);

            if (isUpdated) {
                response.sendRedirect("user-detail");  // Quay lại danh sách người dùng sau khi cập nhật thành công
            } else {
                response.sendRedirect("edit-user?userId=" + userId + "&error=Update failed");  // Nếu cập nhật không thành công, quay lại trang chỉnh sửa
            }
        }
            

    }
}
