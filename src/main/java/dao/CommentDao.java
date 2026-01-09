package dao;

import database.DBConnection;
import model.Comment;
import model.Product;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Lấy danh sách bình luận của một sản phẩm
    public List<Comment> getCommentByProductId(int productId) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT c.comment_id, c.user_id, c.content, c.created_at, c.parent_comment_id, p.product_id, p.name, u.username"
                + " FROM comment c " + " JOIN product p ON c.product_id = p.product_id" + " JOIN user u ON c.user_id = u.user_id" + " WHERE c.product_id = ? "
                + " ORDER BY c.created_at DESC";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) {
                int commentId = rs.getInt("comment_id");
                User user = new User(rs.getInt("user_id"), rs.getString("username"), null, null, null, null); // Chỉ lấy username
                String content = rs.getString("content");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Integer parentCommentId = rs.getObject("parent_comment_id", Integer.class);
                Product product = new Product(rs.getInt("product_id"), rs.getString("name"), null, null, 0, null, null, null, null);

                comments.add(new Comment(commentId, product, user, content, createdAt, parentCommentId));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public void addComment(Comment comment) {
        String query = "INSERT INTO comment (product_id, user_id, content, created_at, parent_comment_id) VALUES (?, ?, ?, ?, ?)";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, comment.getProduct().getProductId());
            ps.setInt(2, comment.getUser().getId());
            ps.setString(3, comment.getContent());
            ps.setTimestamp(4, comment.getcreatedAt());
            ps.setObject(5, comment.getParentCommentId(), Types.INTEGER);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteComment(int commentId, int userId) {
        String query = "SELECT user_id FROM comment WHERE comment_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, commentId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int commentUserId = rs.getInt("user_id");
                if (userId == commentUserId) {
                    // Nếu đúng, tiến hành xoá bình luận
                    String deleteQuery = "DELETE FROM comment WHERE comment_id = ?";
                    try (PreparedStatement psDelete = conn.prepareStatement(deleteQuery)) {
                        psDelete.setInt(1, commentId);
                        psDelete.executeUpdate();
                    }
                } else {
                    throw new IllegalAccessException("Bạn không có quyền xoá bình luận này.");
                }
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void updateComment(Comment comment, int userId) {
        String query = "SELECT user_id FROM comment WHERE comment_id = ?";
        try {
            conn = new DBConnection().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, comment.getCommentId());
            rs = ps.executeQuery();
            if (rs.next()) {
                int commentUserId = rs.getInt("user_id");
                if (userId == commentUserId) {
                    // Nếu đúng, tiến hành cập nhật
                    String updateQuery = "UPDATE comment SET content = ? WHERE comment_id = ?";
                    try (PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {
                        psUpdate.setString(1, comment.getContent());
                        psUpdate.setInt(2, comment.getCommentId());
                        psUpdate.executeUpdate();
                    }
                } else {
                    throw new IllegalAccessException("Bạn không có quyền chỉnh sửa bình luận này.");
                }
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {
        // Tạo đối tượng CommentDao và gọi phương thức getCommentsByProductId
        // Tạo đối tượng CommentDao và gọi phương thức getCommentsByProductId
        CommentDao commentDao = new CommentDao();

        // Thử lấy các bình luận cho một sản phẩm với ID = 1
        int productId = 1;  // Thay đổi theo ID sản phẩm bạn muốn kiểm tra
        List<Comment> comments = commentDao.getCommentByProductId(productId);

        // Hiển thị kết quả
        if (comments.isEmpty()) {
            System.out.println("No comments found for product with ID: " + productId);
        } else {
            for (Comment comment : comments) {
                System.out.println("Comment ID: " + comment.getCommentId());
                System.out.println("Product Name: " + comment.getProduct().getName());
                System.out.println("User: " + comment.getUser().getUsername());
                System.out.println("Comment: " + comment.getContent());
                System.out.println("Created At: " + comment.getcreatedAt());
                System.out.println("Parent Comment ID: " + comment.getParentCommentId());
                System.out.println("---------------");
            }
        }


}
}
