package servlet;

import dao.OrderDao;
import model.Order;
import model.OrderItem;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/order-detail")
public class OrderDetailControl extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy Order ID từ query parameter
        int orderIdStr = Integer.parseInt(request.getParameter("id"));
        // Tạo đối tượng OrderDao để lấy thông tin đơn hàng và chi tiết sản phẩm
        OrderDao orderDao = new OrderDao();
        //Lay thong tin don hang
        Order order = orderDao.getOrderById(orderIdStr);
        //lay chi tiet cac san pham trong don hang
        List<OrderItem> orderItems = orderDao.getOrderItemsByOrderId(orderIdStr);
        // Đưa dữ liệu vào request để chuyển cho JSP
        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        // Chuyển tiếp đến trang order-detail.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("order-detail.jsp");
        dispatcher.forward(request, response);

    }
}
