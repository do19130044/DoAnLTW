package adminDao;

import database.DBConnection;
import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public boolean addUser(User admin, User newUser) {
        if (admin.getRole() == Role.ADMIN) {
            String query = "INSERT INTO `user` (username, password, email, phone, role) VALUES (?, ?, ?, ?, ?)";
            try {
                conn = new DBConnection().getConnection();
                ps = conn.prepareStatement(query);
                ps.setString(1, newUser.getUsername());
                ps.setString(2, newUser.getPassword());
                ps.setString(3, newUser.getEmail());
                ps.setString(4, newUser.getPhone());
                ps.setString(5, newUser.getRole().toString());
                int row = ps.executeUpdate();
                return row > 0;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public boolean updateUser(User admin, User newUser) {
        if (admin.getRole() == Role.ADMIN) {
            String query = "UPDATE `user` SET username = ?, password = ?, email = ?, phone = ?, role = ? WHERE user_id = ?";
            try {
                conn = new DBConnection().getConnection();
                ps = conn.prepareStatement(query);
                ps.setString(1, newUser.getUsername());
                ps.setString(2, newUser.getPassword());
                ps.setString(3, newUser.getEmail());
                ps.setString(4, newUser.getPhone());
                ps.setString(5, newUser.getRole().toString());
                ps.setInt(6, newUser.getId());
                int row = ps.executeUpdate();
                return row > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public boolean deleteUser(User admin, int newUser) {
        if (admin.getRole() == Role.ADMIN) {
            String query = "DELETE FROM `user` WHERE user_id = ?";
            try {
                conn = new DBConnection().getConnection();
                ps = conn.prepareStatement(query);
                ps.setInt(1, newUser);
                int row = ps.executeUpdate();
                return row > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM `user`";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String roleStr = rs.getString("role");
                Role role = Role.valueOf(roleStr); // Giả sử bạn có Role là Enum

                User user = new User(id, username, password, email, phone, role);
                users.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    // Phương thức lấy người dùng theo ID
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM user WHERE user_id = ?";  // Câu lệnh SQL tìm người dùng theo user_id

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Thiết lập giá trị cho tham số trong câu lệnh SQL
            ps.setInt(1, userId);

            // Thực thi truy vấn
            try (ResultSet rs = ps.executeQuery()) {
                // Kiểm tra nếu có dữ liệu trả về
                if (rs.next()) {
                    // Lấy thông tin từ ResultSet và tạo đối tượng User
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String roleStr = rs.getString("role");

                    // Chuyển đổi roleStr thành đối tượng Role
                    Role role = Role.valueOf(roleStr);

                    // Khởi tạo đối tượng User
                    user = new User(userId, username, password, email, phone, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // In ra lỗi nếu có
        }

        return user;  // Trả về đối tượng User (nếu tìm thấy) hoặc null nếu không có
    }

    public int getUserCount() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM `user` WHERE `role` != 'ADMIN'";  // Lấy tổng số người dùng từ bảng `user`

        try {
            // Kết nối đến cơ sở dữ liệu
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);  // Chuẩn bị câu lệnh truy vấn
            rs = ps.executeQuery();  // Thực thi câu lệnh truy vấn

            // Nếu có dữ liệu trả về, lấy kết quả từ query
            if (rs.next()) {
                count = rs.getInt(1);  // Lấy tổng số người dùng từ cột đầu tiên của kết quả query
            }
        } catch (SQLException e) {
            e.printStackTrace();  // In ra lỗi nếu có vấn đề với kết nối hoặc truy vấn
        }  // Đảm bảo đóng kết nối sau khi xong


        return count;  // Trả về tổng số người dùng
    }
}

