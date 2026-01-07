package adminServlet;

import adminDao.OrderDao;
import model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/order-list")
public class OrderControl extends HttpServlet {
    private static final int PAGE_SIZE = 10;
    private OrderDao orderDao;
    @Override
    public void init() {
        orderDao = new OrderDao(); // Tạo đối tượng DAO để truy xuất dữ liệu đơn hàng
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy trang hiện tại từ tham số request, nếu không có thì mặc định là trang 1
        int pageNumber = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                pageNumber = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                pageNumber = 1;
            }
        }
        // Lấy danh sách đơn hàng
        List<Order> orders = orderDao.getAllOrders(pageNumber,PAGE_SIZE);
        //tinh tong so trang
        int totalPages = orderDao.getTotalPages(PAGE_SIZE);
        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", pageNumber);
        request.setAttribute("totalPages", totalPages);
        // Chuyển tiếp đến trang order-list.jsp để hiển thị
        request.getRequestDispatcher("/admin/order-list.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        if(idParam != null) {
            int orderId = Integer.parseInt(idParam);

        if ("acceptOrder".equals(action)) {
            // Cập nhật trạng thái đơn hàng thành 'DELIVERED' (Đồng ý)
            orderDao.updateOrderStatus(orderId, "DELIVERED");
        } else if ("cancelOrder".equals(action)) {
            // Cập nhật trạng thái đơn hàng thành 'CANCELLED' (Huỷ)
            orderDao.updateOrderStatus(orderId, "CANCELLED");
        } else if ("shipOrder".equals(action)) {
            // Cập nhật trạng thái đơn hàng từ 'DELIVERED' thành 'SHIPPED'
            orderDao.updateOrderStatus(orderId, "SHIPPED");
        }   else if ("deleteOrder".equals(action)) {
            // Xóa đơn hàng khỏi cơ sở dữ liệu
            orderDao.deleteOrder(orderId);

        }
            }
        // Sau khi xử lý, chuyển tiếp đến trang hiện tại (order-list.jsp) mà không làm mới trang
        String page = request.getParameter("page"); // Lấy tham số trang từ yêu cầu (nếu có)
        if (page == null) {
            page = "1"; // Trang mặc định nếu không có tham số
        }
//        System.out.println(page);




        // Sau khi xử lý xong, chuyển hướng lại về trang order list
        response.sendRedirect("order-list?page=" + page);

//        response.sendRedirect("order-list");
    }


}
