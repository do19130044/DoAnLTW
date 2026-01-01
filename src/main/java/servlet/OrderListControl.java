package servlet;

import dao.OrderDao;
import model.Order;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/order-list")
public class OrderListControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin người dùng từ session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // Nếu người dùng chưa đăng nhập, chuyển hướng về trang đăng nhập
            response.sendRedirect("login.jsp");
            return;
        }

        // Truy vấn tất cả các đơn hàng của người dùng từ cơ sở dữ liệu
        OrderDao orderDao = new OrderDao();
        List<Order> orders = orderDao.getOrderByUserId(user.getId());

        if (orders.isEmpty()) {
            String lang = (String) session.getAttribute("lang");
            if (lang == null) {
                lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
            }
            // Đặt locale theo ngôn ngữ người dùng chọn
            Locale locale = new Locale(lang);
            ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
            String orderEmpt = messages.getString("order.empty");

            // Nếu không có đơn hàng, bạn có thể thêm một thông báo hoặc xử lý phù hợp
            request.setAttribute("message", orderEmpt);
        }

        // Gửi danh sách đơn hàng đến JSP
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("order-list.jsp").forward(request, response);
    }

    // Xử lý huỷ đơn hàng
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("orderId");
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        if (orderId != null && action != null) {
            OrderDao orderDao = new OrderDao();
//            boolean success = false;
            String lang = (String) session.getAttribute("lang");
            if (lang == null) {
                lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
            }
            // Đặt locale theo ngôn ngữ người dùng chọn
            Locale locale = new Locale(lang);
            ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
            if (action.equals("cancel")) {
                //huy don hang
             boolean success = orderDao.cancelOrder(Integer.parseInt(orderId));
                if (success) {
                    response.sendRedirect("order-list");
                }// Refresh page after cancelling
                else {
                    String orderErr = messages.getString("order.err-cancel");

                    request.setAttribute("message", orderErr);
                    request.getRequestDispatcher("order-list.jsp").forward(request, response);
                }
                } else if (action.equals("delete")) {
                   boolean success = orderDao.deleteOrder(Integer.parseInt(orderId));
                    if (success) {
                        response.sendRedirect("order-list");
                    } else {
                        String orderErr = messages.getString("order.err-del");
                        request.setAttribute("message", orderErr);
                        request.getRequestDispatcher("order-list.jsp").forward(request, response);
                    }
                }


            }
        }
    }

