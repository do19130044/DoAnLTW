package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/electricstore?useUnicode=true&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "";
//    private static final String PASSWORD = "";
    private static Connection connection = null;

    // Phương thức để lấy kết nối
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to database");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Đóng kết nối khi không cần dùng nữa
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Hàm main để kiểm tra kết nối
//    public static void main(String[] args) {
//        // Kiểm tra kết nối
//        try {
//            Connection connection = getConnection();
//
//            if (connection != null && !connection.isClosed()) {
//                System.out.println("Kết nối đến cơ sở dữ liệu thành công!");
//            } else {
//                System.out.println("Không thể kết nối đến cơ sở dữ liệu.");
//            }
//        } catch (SQLException e) {
//            System.out.println("Lỗi khi kết nối đến cơ sở dữ liệu: " + e.getMessage());
//        } finally {
//            // Đảm bảo đóng kết nối sau khi kiểm tra
//            closeConnection();
//        }
//    }
}

