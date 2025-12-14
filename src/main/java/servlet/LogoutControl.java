package servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebServlet("/logout")

public class LogoutControl extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        session.invalidate();

        // Lấy URL trước đó từ Referer hoặc chuyển về trang chủ nếu không có Referer
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty() || !referer.startsWith(request.getContextPath())) {
            referer = request.getContextPath() + "/home";
        }

        // Chuyển hướng về trang trước hoặc trang chủ
        response.sendRedirect(referer);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);}
}
