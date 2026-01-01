package dao;

import database.DBConnection;
import model.Cart;
import model.CartItem;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    // Phương thức lấy giỏ hàng của người dùng từ database (hoặc session nếu có)
    public Cart getCartByUserId(int userId) {
        Cart cart = new Cart();
        String query = "SELECT * FROM cart WHERE user_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                cart.setId(rs.getInt("cart_id"));
                cart.setItems(getCartItems(cart.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    // Lấy danh sách các item trong giỏ hàng
    private List<CartItem> getCartItems(int cartId) {
        List<CartItem> items = new ArrayList<>();
        String query = "SELECT ci.cart_item_id, ci.product_id, ci.quantity, p.name, p.price, p.discount_percentage " +
                "FROM cart_item ci " +
                "JOIN product p ON ci.product_id = p.product_id " +
                "WHERE ci.cart_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, cartId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        null, // description, không cần thiết cho cart
                        rs.getBigDecimal("price"),
                        0, // stock không cần thiết cho cart
                        null, null, null, rs.getBigDecimal("discount_percentage")
                );
                CartItem item = new CartItem(product, rs.getInt("quantity"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Thêm sản phẩm vào giỏ
    public boolean addToCart(int userId, int productId, int quantity) {
        Cart cart = getCartByUserId(userId);

        // Nếu giỏ hàng chưa tồn tại, tạo mới giỏ hàng
        if (cart.getId() == 0) {
            if (!createNewCart(userId)) {
                return false;
            }
            cart = getCartByUserId(userId); // Lấy lại giỏ hàng sau khi tạo mới
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        CartItem existingItem = null;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getProductId() == productId) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            // Nếu sản phẩm đã có trong giỏ, chỉ cần cập nhật số lượng
            return updateCartItemQuantity(cart.getId(), productId, existingItem.getQuantity() + quantity);
        } else {
            // Nếu sản phẩm chưa có trong giỏ, thêm mới vào giỏ hàng
            return addNewCartItem(cart.getId(), productId, quantity);
        }
    }

    // Tạo giỏ hàng mới
    private boolean createNewCart(int userId) {
        String query = "INSERT INTO cart (user_id) VALUES (?)";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm một sản phẩm mới vào giỏ hàng
    private boolean addNewCartItem(int cartId, int productId, int quantity) {
        String query = "INSERT INTO cart_item (cart_id, product_id, quantity) VALUES (?, ?, ?)";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    public boolean updateCartItemQuantity(int cartId, int productId, int newQuantity) {
        String query = "UPDATE cart_item SET quantity = ? WHERE cart_id = ? AND product_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartId);
            ps.setInt(3, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa sản phẩm khỏi giỏ
    public boolean removeFromCart(int cartId, int productId) {
        String query = "DELETE FROM cart_item WHERE cart_id = ? AND product_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tổng số sản phẩm trong giỏ hàng
    public int getTotalItems(int cartId) {
        int total = 0;
        String query = "SELECT SUM(quantity) FROM cart_item WHERE cart_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, cartId);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public static void main(String[] args) {
//        // Giả sử userId là 1 (hoặc ID người dùng mà bạn muốn kiểm tra)
//        //test hien thi gio hang
//        int userId = 3;
//

//        // Tạo đối tượng CartDao để gọi phương thức getCartByUserId
//        CartDao cartDao = new CartDao();
//
//        // Gọi phương thức getCartByUserId để lấy giỏ hàng của người dùng
//        Cart cart = cartDao.getCartByUserId(userId);
//
//        // Kiểm tra nếu giỏ hàng không null và in thông tin giỏ hàng
//        if (cart != null) {
//            System.out.println("Cart ID: " + cart.getId());
//            System.out.println("Items in Cart:");
//            List<CartItem> cartItems = cart.getItems();
//            if (cartItems != null && !cartItems.isEmpty()) {
//                for (CartItem item : cartItems) {
//                    System.out.println("Product ID: " + item.getProduct().getProductId() +
//                            ", Quantity: " + item.getQuantity() +
//                            ", Total Price: " + item.getTotalPrice());
//                }
//            } else {
//                System.out.println("No items in the cart.");
//            }
//        } else {
//            System.out.println("No cart found for user with ID " + userId);
//        }
//    }
//        //test chuc nang them gio hang
//        // Tạo đối tượng CartDao
//        CartDao cartDao = new CartDao();
//
//        // Các giá trị giả lập cho userId, productId và quantity
//        int userId = 3; // Giả lập userId = 1
//        int productId = 1; // Giả lập productId = 101
//        int quantity = 2; // Giả lập số lượng sản phẩm cần thêm vào giỏ hàng
//
//        // Test thêm sản phẩm vào giỏ hàng
//        boolean result = cartDao.addToCart(userId, productId, quantity);
//
//        // Kiểm tra kết quả
//        if (result) {
//            System.out.println("Sản phẩm đã được thêm vào giỏ hàng thành công.");
//        } else {
//            System.out.println("Thêm sản phẩm vào giỏ hàng thất bại.");
//        }
//
//        // Kiểm tra giỏ hàng của người dùng
//        Cart cart = cartDao.getCartByUserId(userId);
//        if (cart != null) {
//            System.out.println("Giỏ hàng của người dùng ID: " + userId);
//            for (CartItem item : cart.getItems()) {
//                System.out.println("Sản phẩm: " + item.getProduct().getProductId() + " - "
//                        + item.getProduct().getName() + " | Số lượng: " + item.getQuantity());
//            }
//        } else {
//            System.out.println("Giỏ hàng của người dùng không tồn tại.");
//        }
//    }
        CartDao cartDao = new CartDao();

        // Các giá trị giả lập để kiểm tra
        int cartId = 1;       // ID của giỏ hàng, giả sử giỏ hàng có ID = 1
        int productId = 2;    // ID sản phẩm, giả sử sản phẩm có ID = 2
        int newQuantity = 11;  // Số lượng mới cần cập nhật

        // Gọi phương thức updateCartItemQuantity để cập nhật số lượng sản phẩm
        boolean result = cartDao.updateCartItemQuantity(cartId, productId, newQuantity);

        // Kiểm tra kết quả
        if (result) {
            System.out.println("Cập nhật số lượng sản phẩm thành công.");
        } else {
            System.out.println("Cập nhật số lượng sản phẩm thất bại.");
        }
    }
    }


