package filter;

import model.Role;
import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")

public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Lấy URL yêu cầu
        String requestURI = httpRequest.getRequestURI();


        // Kiểm tra nếu người dùng đã đăng nhập nhưng lại truy cập trang login hoặc register
        if (session != null && session.getAttribute("user") != null) {
            //   session.setAttribute("redirectUrl", requestURI);
            // Nếu người dùng đã đăng nhập và đang cố truy cập /login hoặc /register
            if (requestURI.contains("/login") || requestURI.contains("/register")) {
                // Chuyển hướng về trang home, mã hóa session vào URL
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
                return; // Dừng việc xử lý và không cho phép tiếp tục vào các trang này
            }
        }

        // Nếu người dùng chưa đăng nhập và yêu cầu truy cập các trang bảo vệ
        if (isProtectedPage(requestURI)) {
            if (session == null || session.getAttribute("user") == null) {
                // Nếu không có session, chuyển hướng người dùng về trang login
                // Sử dụng encodeURL để đảm bảo session ID được mã hóa vào URL
                String loginURL = httpRequest.getContextPath() + "/login";
                httpResponse.sendRedirect(httpResponse.encodeRedirectURL(loginURL));
                return; // Ngừng xử lý và trả về
            }

            // Nếu có session, lấy role người dùng từ session
            User user = (User) session.getAttribute("user");
            Role role = user.getRole();
            //  String role = (String) session.getAttribute("role");  // Lấy role từ session
            // session.setAttribute("redirectUrl", requestURI);

            // Kiểm tra quyền truy cập dựa trên role
            if (requestURI.contains("/admin/") && (role == null || !role.equals(Role.ADMIN))) {
                // Nếu yêu cầu trang admin nhưng người dùng không phải ADMIN
                String accessDeniedURL = httpRequest.getContextPath() + "/home";
                httpResponse.sendRedirect(httpResponse.encodeRedirectURL(accessDeniedURL));
                return; // Ngừng xử lý và trả về
            } else if (!requestURI.contains("/admin/") && role != null && role.equals(Role.ADMIN)) {
                // Tránh trường hợp ADMIN truy cập các trang của CUSTOMER mà không muốn
                if (!requestURI.contains("/admin/")) {
                    // Trường hợp không phải ADMIN mà truy cập vào các trang chỉ dành cho ADMIN
                    String accessDeniedURL = httpRequest.getContextPath() + "/admin/home";
                    httpResponse.sendRedirect(httpResponse.encodeRedirectURL(accessDeniedURL));
                    return;
                }
            }
        }

        // Tiếp tục xử lý chuỗi filter (đi qua các filter tiếp theo)
        chain.doFilter(request, response);
    }

    // Kiểm tra xem URL có thuộc các trang cần bảo vệ không
    private boolean isProtectedPage(String requestURI) {
        return requestURI.contains("/cart") || requestURI.contains("/checkout")
                || requestURI.contains("/orders") || requestURI.contains("/admin/");
    }


    @Override
    public void destroy() {

    }
}
