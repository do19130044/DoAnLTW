package adminServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebServlet("/admin/logout")

public class LogoutControl extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        String referer = request.getHeader("Referer");

        // Nếu không có Referer (trang hiện tại không có header), mặc định chuyển về trang chủ
        if (referer == null || referer.isEmpty() ) {
            referer = request.getContextPath() + "/home";
        }

        // Chuyển hướng về trang hiện tại
        response.sendRedirect(referer);
    }
}

