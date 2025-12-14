package dao;

import database.DBConnection;
import model.Role;
import model.User;

import java.io.File;
import java.sql.*;

public class UserDao {

//    public UserDao(Connection connection) {
//        this.connection = connection;
//    }

    // Phương thức lấy thông tin người dùng theo username
    public User getUserByUsername(String username) {
        User user = null;
        String sql = "SELECT * FROM user WHERE username = ?";  // Câu lệnh SQL truy vấn người dùng theo username

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);  // Đặt giá trị cho tham số trong câu lệnh SQL

            ResultSet rs = stmt.executeQuery();  // Thực hiện truy vấn

            // Nếu có kết quả, lấy thông tin người dùng từ ResultSet
            if (rs.next()) {
                int id = rs.getInt("user_id");
                String userName = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String roleStr = rs.getString("role");

                // Giả sử bạn có lớp Role, bạn có thể chuyển đổi roleStr thành đối tượng Role
                Role role = Role.valueOf(roleStr);  // Cần phải xử lý nếu role là enum

                // Tạo đối tượng User và gán giá trị
                user = new User(id, userName, password, email, phone, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log lỗi nếu có
        }

        return user;  // Trả về đối tượng User nếu tìm thấy, nếu không thì null
    }
    // Phương thức kiểm tra người dùng đã tồn tại chưa dựa trên username hoặc email
    public boolean checkUserExists(String username, String email) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM `user` WHERE username = ? OR email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    // Phương thức lưu thông tin người dùng vào cơ sở dữ liệu
    public boolean saveUser(User user) {
        boolean isSaved = false;
        String sql = "INSERT INTO `user` (username, password, email, phone, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole().toString());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                isSaved = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSaved;
    }

    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM user WHERE user_id = ?";  // Câu lệnh SQL truy vấn người dùng theo username

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(userId));  // Đặt giá trị cho tham số trong câu lệnh SQL

            ResultSet rs = stmt.executeQuery();  // Thực hiện truy vấn

            // Nếu có kết quả, lấy thông tin người dùng từ ResultSet
            if (rs.next()) {
                int id = rs.getInt("user_id");
                String userName = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String roleStr = rs.getString("role");

                // Giả sử bạn có lớp Role, bạn có thể chuyển đổi roleStr thành đối tượng Role
                Role role = Role.valueOf(roleStr);  // Cần phải xử lý nếu role là enum

                // Tạo đối tượng User và gán giá trị
                user = new User(id, userName, password, email, phone, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log lỗi nếu có
        }

        return user;  // Trả về đối tượng User nếu tìm thấy, nếu không thì null
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE user SET username = ?, email = ?, phone = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setInt(4, user.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Nếu có dòng bị ảnh hưởng, tức là cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Tìm người dùng theo email
    public User findByEmail(String email) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM user WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        Role.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lưu token reset mật khẩu và thời gian hết hạn vào cơ sở dữ liệu
    public void saveResetToken(int userId, String token, Date expiryDate) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "UPDATE user SET reset_token = ?, reset_token_expiry = ? WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, token);
            ps.setTimestamp(2, new Timestamp(expiryDate.getTime())); // Lưu thời gian hết hạn
            ps.setInt(3, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra token và lấy người dùng
    public User findByResetToken(String token) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM user WHERE reset_token = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Kiểm tra nếu token đã hết hạn
                Timestamp expiry = rs.getTimestamp("reset_token_expiry");
                if (expiry != null && expiry.before(new Timestamp(System.currentTimeMillis()))) {
                    return null; // Token hết hạn
                }

                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        Role.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật mật khẩu người dùng QUÊN MK
    public void updateFPassword(int userId, String newPassword) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "UPDATE user SET password = ?, reset_token = NULL, reset_token_expiry = NULL WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Cập nhật mật khẩu người dùng
    public boolean updatePassword(int userId, String newPassword) {

        String sql = "UPDATE user SET password = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
//        try {
//            // Tạo kết nối đến cơ sở dữ liệu
//           UserDao userDao = new UserDao();
//
//            // Kiểm tra phương thức getUserByUsername
//            String usernameToSearch = "chuotcon"; // Giả sử đây là username bạn muốn tìm
//
//            User user = userDao.getUserByUsername(usernameToSearch);
//
//            if (user != null) {
//                System.out.println("Thông tin người dùng: " + user);
//            } else {
//                System.out.println("Không tìm thấy người dùng với username: " + usernameToSearch);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        File file = new File(".env");
        System.out.println("File exists: " + file.exists());
        System.out.println(System.getenv("SENDGRID_API_KEY"));



    }
}


//    public void addProduct(Product product) {
//        String query = "INSERT INTO products (name, description, price, image) VALUES (?, ?, ?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, product.getName());
//            stmt.setString(2, product.getDescription());
//            stmt.setDouble(3, product.getPrice());
//            stmt.setString(4, product.getImagePath());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
