package dao;

import database.DBConnection;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    static Connection conn = null;
    static PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Product> getAllProduct() {
        List<Product> list = new ArrayList<Product>();
        String query = "select * from product";
        try {
            conn = new DBConnection().getConnection(); //mo ket noi database
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product temp = new Product(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBigDecimal(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getTimestamp(8),
                        rs.getBigDecimal(9)
                );
                list.add(temp);
            }

        } catch (Exception e) {

        }
        return list;
    }


    public int getTotalProduct() {
        String query = "select count(*) from product";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {

        }
        return 0;
    }
    public int getTotalPages(int pageSize) {
        int totalProduct = getTotalProduct();
        return (int) Math.ceil((double) totalProduct / pageSize);
    }

    public List<Product> getAllProductPaging(int page, int total ) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM product ORDER BY product_id LIMIT ? OFFSET ?";

        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, total); // Tính offset
            ps.setInt(2, (page - 1) * total); // Lấy số sản phẩm mỗi trang
            rs = ps.executeQuery();

            while (rs.next()) {
                Product temp = new Product(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBigDecimal(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getTimestamp(8),
                        rs.getBigDecimal(9)
                );
                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }
    public List<Product> searchProductPaging(String keyword, int total, int page) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM product WHERE name LIKE ? ORDER BY product_id LIMIT ? OFFSET ?";
        try {
            keyword = keyword.replace(" ", "%"); // Thay thế dấu cách bằng ký tự đại diện '%'
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%"); // Tìm kiếm theo tên sản phẩm
            ps.setInt(2, total);  // Số sản phẩm mỗi trang
            ps.setInt(3, (page - 1) * total);  // Tính offset
            rs = ps.executeQuery();

            while (rs.next()) {
                Product temp = new Product(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBigDecimal(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getTimestamp(8),
                        rs.getBigDecimal(9)
                );
                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tổng số sản phẩm dựa trên từ khóa tìm kiếm
    public int getTotalProductBySearch(String keyword) {
        String query = "SELECT count(*) FROM product WHERE name LIKE ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getTotalPagesBySearch(String keyword, int productsPerPage) {
        int totalProducts = getTotalProductBySearch(keyword);
        return (int) Math.ceil((double) totalProducts / productsPerPage);  // Tính số trang tổng
    }
    public static int getStockByProductId(int productId) throws SQLException {
        String query = "SELECT stock FROM product WHERE product_id = ?";
        int quantityInStock = 0;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            // Kiểm tra nếu có kết quả trả về
            if (rs.next()) {
                quantityInStock = rs.getInt("stock");
            }
        }
        return quantityInStock;
    }

    public Product getProductByID(int id) {
        String query = "SELECT * FROM product WHERE product_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet  rs = ps.executeQuery();
            if(rs.next()) {
                return new Product(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBigDecimal(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getTimestamp(8),
                        rs.getBigDecimal(9));
            }

        } catch (SQLException e) {

        }
        return null;
    }
    public void updateProductStock(int productId, int newStock) {
        String query = "UPDATE product SET stock = stock - ? WHERE product_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            ps.executeUpdate();
            if(newStock == 0) {
                deleteProduct(productId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Product> getProductByCategoryPaging(String category, int page, int total) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM product WHERE category = ? ORDER BY product_id LIMIT ? OFFSET ?";
        try{
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, category);
            ps.setInt(2, total);
            ps.setInt(3, (page - 1) * total);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product temp = new Product(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBigDecimal(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getTimestamp(8),
                        rs.getBigDecimal(9));
                list.add(temp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public int getTotalProductByCategory(String category) {
        String query = "SELECT count(*) FROM product WHERE category = ?";
        try{
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, category);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    // Tính số trang cho tìm kiếm theo category
    public int getTotalPagesByCategory(String category, int productsPerPage) {
        int totalProducts = getTotalProductByCategory(category);
        return (int) Math.ceil((double) totalProducts / productsPerPage);  // Tính số trang tổng
    }
    public List<Product> getNewestProduct() {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM product ORDER BY created_at DESC LIMIT 10";// Truy vấn lấy 10 sản phẩm mới nhất
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product temp = new Product(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBigDecimal(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getTimestamp(8),
                        rs.getBigDecimal(9));
                productList.add(temp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productList;
    }

    public void deleteProduct(int productId) {
        String query = "DELETE FROM product WHERE product_id = ?";
        try{
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
             ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Product> getAllProductPagingSorted(int page, int total, String sortProduct) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM product ORDER BY price " + sortProduct.toUpperCase() + " LIMIT ? OFFSET ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, total);
            ps.setInt(2, (page - 1) * total);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getBigDecimal(4), rs.getInt(5), rs.getString(6),
                        rs.getString(7), rs.getTimestamp(8), rs.getBigDecimal(9)
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Sắp xếp theo giá đã giảm (discounted price)
        if ("desc".equalsIgnoreCase(sortProduct)) {
            list.sort((p1, p2) -> p2.getDiscountedPrice().compareTo(p1.getDiscountedPrice()));
        } else {
            list.sort((p1, p2) -> p1.getDiscountedPrice().compareTo(p2.getDiscountedPrice()));
        }
        return list;
    }
    public List<Product> getProductByCategoryPagingSorted(String category, int page, int total, String sortProduct) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM product WHERE category = ? ORDER BY price " + sortProduct.toUpperCase() + " LIMIT ? OFFSET ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, category);
            ps.setInt(2, total);
            ps.setInt(3, (page - 1) * total);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getBigDecimal(4), rs.getInt(5), rs.getString(6),
                        rs.getString(7), rs.getTimestamp(8), rs.getBigDecimal(9)
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Sắp xếp theo giá đã giảm (discounted price)
        if ("desc".equalsIgnoreCase(sortProduct)) {
            list.sort((p1, p2) -> p2.getDiscountedPrice().compareTo(p1.getDiscountedPrice()));
        } else {
            list.sort((p1, p2) -> p1.getDiscountedPrice().compareTo(p2.getDiscountedPrice()));
        }
        return list;
    }
    public List<Product> searchProductPagingSort(String keyword, int total, int page, String sortProduct) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM product WHERE name LIKE ? ORDER BY price " + sortProduct.toUpperCase() + " LIMIT ? OFFSET ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, total);
            ps.setInt(3, (page - 1) * total);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getBigDecimal(4), rs.getInt(5), rs.getString(6),
                        rs.getString(7), rs.getTimestamp(8), rs.getBigDecimal(9)
                );
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Sắp xếp theo giá đã giảm (discounted price)
        if ("desc".equalsIgnoreCase(sortProduct)) {
            list.sort((p1, p2) -> p2.getDiscountedPrice().compareTo(p1.getDiscountedPrice()));
        } else {
            list.sort((p1, p2) -> p1.getDiscountedPrice().compareTo(p2.getDiscountedPrice()));
        }
        return list;
    }
    // Cập nhật số lượng sản phẩm trong kho
    public static void updatStock(int productId, int newStock) {
        String query = "UPDATE product SET stock = stock + ? WHERE product_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            ps.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        // Thực hiện kiểm tra với các tham số
        String keyword = "Product";
        int page = 1;              // Trang hiện tại
        int total = 10;            // Số sản phẩm mỗi trang
        String sortProduct = "desc"; // Sắp xếp theo giá tăng dần ("asc" hoặc "desc")

        // Tạo đối tượng ProductDao để gọi phương thức getAllProductPagingSorted
        ProductDao productDao = new ProductDao();

        // Gọi phương thức lấy sản phẩm với sắp xếp theo giá
        List<Product> productList = productDao.searchProductPagingSort(keyword,total,page,sortProduct);

        // Kiểm tra và in ra kết quả
        if (productList.isEmpty()) {
            System.out.println("Không có sản phẩm nào được tìm thấy!");
        } else {
            System.out.println("Danh sách sản phẩm (sắp xếp theo giá " + sortProduct + "): ");
            for (Product product : productList) {
                System.out.println("ID: " + product.getProductId() +
                        ", Tên: " + product.getName() +
                        ", Giá: " + product.getPrice() +
                        ", Danh mục: " + product.getCategory());
            }
        }
    }
    }





