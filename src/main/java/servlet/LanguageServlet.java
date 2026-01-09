package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/language")
public class LanguageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lang = request.getParameter("lang");

        if (lang != null) {
            // Lưu ngôn ngữ vào session scope
            request.getSession().setAttribute("lang", lang);
        }

        // Chuyển hướng về trang trước đó hoặc trang home sau khi thay đổi ngôn ngữ
        String referer = request.getHeader("Referer");
        if (referer != null) {
            response.sendRedirect(referer);  // Quay lại trang trước đó
        } else {
            response.sendRedirect(request.getContextPath() + "/home");  // Nếu không có trang trước đó, chuyển đến trang home
        }
    }
}

