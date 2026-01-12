package adminDao;

import database.DBConnection;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    // Hàm lấy tất cả sản phẩm với phân trang
    public List<Product> getAllProducts(int page, int pageSize) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product LIMIT ?, ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Tính toán offset
            int offset = (page - 1) * pageSize;
            statement.setInt(1, offset);
            statement.setInt(2, pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getInt("stock"),
                        resultSet.getString("category"),
                        resultSet.getString("imagePath"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getBigDecimal("discount_percentage")
                );
                products.add(product);
            }
        }
        return products;
    }

    // Hàm đếm tổng số sản phẩm (để phân trang)
    public int getTotalProducts() throws SQLException {
        String query = "SELECT COUNT(*) FROM product";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public boolean addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO product (name, description, price, stock, category, imagePath, discount_percentage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, product.getStock());
            statement.setString(5, product.getCategory());
            statement.setString(6, product.getImagePath());
            statement.setBigDecimal(7, product.getDiscountPercentage());
            return statement.executeUpdate() > 0;
        }
    }
    // Lấy sản phẩm theo ID
    public Product getProductById(int productId) {
        Product product = null;
        String sql = "SELECT * FROM product WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getInt("stock"),
                        resultSet.getString("category"),
                        resultSet.getString("imagePath"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getBigDecimal("discount_percentage")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
    // Xóa Sản Phẩm
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Cập nhật sản phẩm
    public boolean updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, description = ?, price = ?, stock = ?, category = ?, imagePath = ?, discount_percentage = ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, product.getStock());
            statement.setString(5, product.getCategory());
            statement.setString(6, product.getImagePath());

            statement.setBigDecimal(7, product.getDiscountPercentage());
            statement.setInt(8, product.getProductId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


//    public boolean updateProduct(Product product) throws SQLException {
//        // Câu lệnh SQL cập nhật các thông tin sản phẩm
//        String sql = "UPDATE product SET name = ?, description = ?, price = ?, category = ?, stock = ?, discount_percentage = ?, imagePath = ? WHERE 	product_id = ?";
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement statement = conn.prepareStatement(sql)) {
//
//            // Set các giá trị từ đối tượng Product vào câu lệnh SQL
//            statement.setString(1, product.getName());
//            statement.setString(2, product.getDescription());
//            statement.setBigDecimal(3, product.getPrice());
//            statement.setString(4, product.getCategory());
//            statement.setInt(5, product.getStock());
//            statement.setBigDecimal(6, product.getDiscountPercentage());
//
//            // Kiểm tra nếu có ảnh mới thì set lại, nếu không có thì không thay đổi trường imagePath
//            if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
//                statement.setString(7, product.getImagePath());  // Cập nhật ảnh mới nếu có
//            } else {
//                // Nếu không có ảnh mới, giữ nguyên giá trị cũ trong database (không set lại imagePath)
//                statement.setString(7, null);  // Hoặc để null nếu không có ảnh mới
//            }
//
//            // Set ID sản phẩm để cập nhật đúng sản phẩm
//            statement.setInt(8, product. getProductId());
//
//            // Thực thi câu lệnh SQL và trả về kết quả
//            return statement.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
//        }
//    }

    // Hàm lưu ảnh vào thư mục /img/ và trả về tên file ảnh
//    private String saveImage(Part imagePart) {
//        String fileName = imagePart.getSubmittedFileName();
//        String saveDirectory = "D:/LT_FE/ESC/src/main/webapp/img"; // Đường dẫn tới thư mục img trong webapp
//        String imagePath = "img/" + fileName; // Lưu trữ tên file ảnh vào cơ sở dữ liệu
//
//        try {
//            Path filePath = Paths.get(saveDirectory, fileName);
//            Files.copy(imagePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);  // Lưu ảnh vào thư mục img
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return fileName;  // Trả về tên file ảnh để lưu vào cơ sở dữ liệu
//    }
//    public boolean addProductTest(Product product, String imagePath) {
//        boolean isSuccess = false;
//// Lưu ảnh và lấy đường dẫn (tên file ảnh)
//        String imagePaths = saveImageTest(imagePath);
//        // Kết nối đến cơ sở dữ liệu
//        String query = "INSERT INTO product (name, description, price, stock, category, imagePath, discount_percentage) VALUES (?, ?, ?, ?, ?, ?, ?)";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            // Set các tham số cho câu truy vấn
//            stmt.setString(1, product.getName());
//            stmt.setString(2, product.getDescription());
//            stmt.setBigDecimal(3, product.getPrice());
//            stmt.setInt(4, product.getStock());
//            stmt.setString(5, product.getCategory());
//            stmt.setString(6, imagePath);  // Lưu tên file ảnh
//            stmt.setBigDecimal(7, product.getDiscountPercentage());
//
//            int rowsAffected = stmt.executeUpdate();
//            if (rowsAffected > 0) {
//                isSuccess = true;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return isSuccess;
//    }
//    private String saveImageTest(String imageName) {
//        // Đây là giả lập việc lưu ảnh vào thư mục
//        String saveDirectory = "D:/LT_FE/ESC/src/main/webapp/img";  // Đường dẫn tới thư mục img
//        String imagePath = "img/" + imageName;  // Lưu trữ tên file ảnh vào cơ sở dữ liệu
//
//        // Giả lập việc lưu ảnh (ở đây không thực sự cần thiết)
//        System.out.println("Lưu ảnh vào: " + saveDirectory + "/" + imageName);
//
//        return imagePath;  // Trả về tên file ảnh để lưu vào cơ sở dữ liệu
//    }
//
//    // Hàm main để kiểm tra chức năng thêm sản phẩm
//    public static void main(String[] args) {
//        ProductDao dao = new ProductDao();
//
//        // Tạo đối tượng Product
//        Product product = new Product();
//        product.setName("Sản phẩm 10");
//        product.setDescription("Mô tả sản phẩm 1");
//        product.setPrice(new BigDecimal("100.00"));
//        product.setStock(10);
//        product.setCategory("Điện tử");
//        product.setDiscountPercentage(new BigDecimal("10.00"));
//
//        // Giả sử bạn đã có một Part đối tượng hình ảnh từ frontend, đây chỉ là một ví dụ
//        String imagePath = "product1.jpg"; // Bạn có thể chỉ cần truyền tên ảnh mà không cần sử dụng Part
//
//        // Thêm sản phẩm vào cơ sở dữ liệu
//        boolean result = dao.addProductTest(product, imagePath);
//
//        if (result) {
//            System.out.println("Sản phẩm đã được thêm thành công.");
//        } else {
//            System.out.println("Có lỗi khi thêm sản phẩm.");
//        }
//    }
}

