package dao;

import database.DBConnection;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    static Connection conn = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;

    // Thêm một đơn hàng vào bảng "order"
        public int addOrder(Order order) {
            String query = "INSERT INTO `order` (user_id, total_price, shipping_address, status, order_date) VALUES (?, ?, ?, ?, ?)";
            try {
                conn = new DBConnection().getConnection();
                ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, order.getUserId());
                ps.setBigDecimal(2, order.getTotalPrice());
                ps.setString(3, order.getShippingAddress());
                ps.setString(4, order.getStatus());
                ps.setTimestamp(5, order.getOrderDate());
                ps.executeUpdate();

                // Lấy ID của đơn hàng vừa được tạo
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
            return 0; // Trả về 0 nếu không tạo được đơn hàng
        }

    // Thêm chi tiết sản phẩm vào bảng "order_item"
    public void addOrderItems(List<OrderItem> orderItems) {
        String query = "INSERT INTO `order_item` (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            for (OrderItem item : orderItems) {
                ps.setInt(1, item.getOrderId());
                ps.setInt(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getPrice());
                ps.addBatch();
            }
            ps.executeBatch(); // Thực thi batch để thêm tất cả chi tiết sản phẩm
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public Order getOrderById(int orderId) {
        String query = "SELECT o.*, u.user_id AS user_id, u.username,u.password, u.email, u.phone, u.role " +
                "FROM `order` o " +
                "JOIN `user` u ON o.user_id = u.user_id " +  // Đảm bảo cột user_id khớp giữa các bảng
                "WHERE o.order_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String roleName = rs.getString("role");
                Role role = Role.valueOf(roleName);
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("shipping_address"),
                        rs.getString("status"),
                        rs.getTimestamp("order_date"));
                // tao doi tuong User
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        role);

                order.setCustomer(user);

                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return null;
    }

    // Lấy danh sách các đơn hàng
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `order`";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("shipping_address"),
                        rs.getString("status"),
                        rs.getTimestamp("order_date")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return orders;
    }

    // Lấy danh sách chi tiết đơn hàng theo order_id
    public static List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT oi.*, p.product_id, p.name, p.price, p.description, p.stock, p.category, p.imagePath,p.created_at, p.discount_percentage\n" +
                "FROM `order_item` oi\n" +
                "JOIN `product` p ON oi.product_id = p.product_id\n" +
                "WHERE oi.order_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("price"));

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock"),
                        rs.getString("category"),
                        rs.getString("imagePath"),
                        rs.getTimestamp("created_at"),
                        rs.getBigDecimal("discount_percentage"));

                item.setProduct(product);




                orderItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return orderItems;
    }

    // Đóng kết nối
    private static void closeConnection() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public  List<Order> getOrderByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `order` WHERE user_id = ?";// Lọc các đơn hàng chưa bị hủy
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order(rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("shipping_address"),
                        rs.getString("status"),
                        rs.getTimestamp("order_date"));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return orders;
        }
        public boolean cancelOrder(int orderId) {
            String query = "UPDATE `order` SET status = 'CANCELLED' WHERE order_id = ? AND status = 'PENDING'";
            try {
                List<OrderItem> orderItems = OrderDao.getOrderItemsByOrderId(orderId);
                // Cập nhật lại kho cho từng sản phẩm trong đơn hàng
                for (OrderItem item : orderItems) {
                   ProductDao.updatStock(item.getProductId(), item.getQuantity());
                }
                conn = new DBConnection().getConnection();
                ps = conn.prepareStatement(query);
                ps.setInt(1, orderId);
                int rows = ps.executeUpdate();
                return rows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        }
    public boolean deleteOrder(int orderId) {
        String query = "DELETE FROM `order` WHERE order_id = ? AND status = 'CANCELLED'";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            int rows = ps.executeUpdate();
            return rows > 0;  // Trả về true nếu đơn hàng đã được xoá
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Trả về false nếu có lỗi
        } finally {
            closeConnection();
        }
    }
    //xoa san pham trong gio hang
    public void clearCart(int userId) {
        String query = "DELETE FROM cart WHERE user_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.executeUpdate();  // Xoá tất cả sản phẩm trong giỏ hàng của người dùng
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
    public static void main(String[] args) {
//        // Khởi tạo đối tượng Order với dữ liệu giả
//        int userId = 3;  // ID người dùng
////        BigDecimal totalPrice = new BigDecimal("150.75");  // Tổng giá trị đơn hàng
////        String shippingAddress = "123 Main St, City, Country";  // Địa chỉ giao hàng
////        String status = "PENDING";  // Trạng thái đơn hàng
////        Timestamp orderDate = new Timestamp(System.currentTimeMillis());  // Ngày đặt hàng
////
////        Order order = new Order(0, userId, totalPrice, shippingAddress, status, orderDate); // orderId sẽ được tự động tạo bởi DB
////
////        // Khởi tạo đối tượng OrderDao để thêm đơn hàng
////        OrderDao orderDao = new OrderDao();
////
////        // Gọi phương thức addOrder để thêm đơn hàng vào cơ sở dữ liệu
////        int orderId = orderDao.addOrder(order);
////
////        // Kiểm tra và in ra kết quả
////        if (orderId > 0) {
////            System.out.println("Đơn hàng đã được tạo thành công với ID: " + orderId);
////        } else {
////            System.out.println("Không thể tạo đơn hàng.");
////        }
////    }
//
//
//            OrderDao orderDao = new OrderDao();
//            List<Order> orders = orderDao.getOrderByUserId(userId);
//
//            if (orders.isEmpty()) {
//                System.out.println("Không có đơn hàng nào cho userId " + userId);
//            } else {
//                System.out.println("Danh sách đơn hàng cho userId " + userId + ":");
//                for (Order order : orders) {
//                    System.out.println("Mã đơn hàng: " + order.getOrderId());
//                    System.out.println("Tổng tiền: " + order.getTotalPrice() + " VND");
//                    System.out.println("Địa chỉ giao hàng: " + order.getShippingAddress());
//                    System.out.println("Trạng thái: " + order.getStatus());
//                    System.out.println("Ngày đặt: " + order.getOrderDate());
//                    System.out.println("------------------------");
//                }
//            }
//        }

            // Tạo đối tượng OrderDao
//            OrderDao orderDao = new OrderDao();
//
//            // Chỉ định ID của đơn hàng cần huỷ
//            int orderId = 1; // Thay thế với ID đơn hàng thực tế bạn muốn kiểm tra
//
//            // Gọi phương thức cancelOrder và kiểm tra kết quả
//            boolean result = orderDao.cancelOrder(orderId);
//
//            // In ra kết quả
//            if (result) {
//                System.out.println("Đơn hàng " + orderId + " đã được huỷ thành công.");
//            } else {
//                System.out.println("Không thể huỷ đơn hàng " + orderId + ".");
//            }
//        }
        // Create an instance of OrderDao
//        OrderDao orderDao = new OrderDao();
//
//        // Set an example orderId that you want to test with
//        int orderId = 57; // Replace with a valid orderId that exists in your database
//
//        // Call the getOrderById method to fetch the order by its ID
//        Order order = orderDao.getOrderById(orderId);
//
//        // Check if the order is null, which means no order was found for the given orderId
//        if (order != null) {
//            // Print order details
//            System.out.println("Order ID: " + order.getOrderId());
//            System.out.println("User ID: " + order.getUserId());
//            System.out.println("Total Price: " + order.getTotalPrice());
//            System.out.println("Shipping Address: " + order.getShippingAddress());
//            System.out.println("Status: " + order.getStatus());
//            System.out.println("Order Date: " + order.getOrderDate());
//
//            // Print customer details (user details associated with the order)
//            User customer = order.getCustomer();
//            if (customer != null) {
//                System.out.println("Customer ID: " + customer.getId());
//                System.out.println("Username: " + customer.getUsername());
//                System.out.println("Email: " + customer.getEmail());
//                System.out.println("Phone: " + customer.getPhone());
//                System.out.println("Role: " + customer.getRole());
//            }
//        } else {
//            // If no order was found, print a message
//            System.out.println("No order found with ID: " + orderId);
//        }
//    }
        // Khởi tạo đối tượng OrderDao để gọi phương thức getOrderItemsByOrderId
        OrderDao orderDao = new OrderDao();

        // Chỉ định orderId mà bạn muốn kiểm tra
        int orderId = 60; // Thay thế bằng ID đơn hàng thực tế bạn muốn kiểm tra

        // Gọi phương thức để lấy danh sách OrderItem cho orderId
        List<OrderItem> orderItems = orderDao.getOrderItemsByOrderId(orderId);

        // Kiểm tra và in ra kết quả
        if (orderItems != null && !orderItems.isEmpty()) {
            System.out.println("Danh sách sản phẩm trong đơn hàng với orderId " + orderId + ":");
            for (OrderItem item : orderItems) {
                System.out.println("Mã đơn hàng: " + item.getOrderId());
                System.out.println("Mã sản phẩm: " + item.getProduct().getProductId());
                System.out.println("Tên sản phẩm: " + item.getProduct().getName());
                System.out.println("Số lượng: " + item.getQuantity());
                System.out.println("Giá sản phẩm: " + item.getPrice() + " VND");
                System.out.println("----------------------------");
            }
        } else {
            System.out.println("Không tìm thấy sản phẩm cho đơn hàng có orderId: " + orderId);
        }
    }
    }

