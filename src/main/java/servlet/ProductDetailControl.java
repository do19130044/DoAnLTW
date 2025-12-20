package servlet;

import dao.CommentDao;
import dao.ProductDao;
import model.Comment;
import model.Product;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/productdetail")
public class ProductDetailControl extends HttpServlet {
    private ProductDao productDao;
    private CommentDao commentDao;
    public void init() throws ServletException {
        productDao = new ProductDao();
        commentDao = new CommentDao();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Product product = productDao.getProductByID(id);
                if (product != null) {
                    List<Comment> comments = commentDao.getCommentByProductId(id);
                    request.setAttribute("product", product);
                    request.setCharacterEncoding("UTF-8");
                    response.setContentType("text/html; charset=UTF-8");
                    request.setAttribute("comments", comments);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("product-detail.jsp");
                    dispatcher.forward(request, response);
                    return;


            }
        } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("product");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String productIdParam = request.getParameter("productId");

        try {
            int productId = Integer.parseInt(productIdParam);

            // Lấy thông tin người dùng từ session
            User user = (User) request.getSession().getAttribute("user");

            if (user == null) {
                response.sendRedirect("login");
                return;
            }

            if ("add".equals(action)) {
                // Xử lý thêm bình luận
                String content = request.getParameter("content");
                if (content != null && !content.trim().isEmpty()) {
                    Comment comment = new Comment();
                    comment.setProduct(new Product(productId, null, null, null, 0, null, null, null, null));
                    comment.setUser(user);
                    comment.setContent(content.trim());
                    comment.setcreatedAt(new Timestamp(System.currentTimeMillis()));
                    comment.setParentCommentId(null);

                    commentDao.addComment(comment);
                }
            } else if ("delete".equals(action)) {
                // Xử lý xóa bình luận
                String commentIdParam = request.getParameter("commentId");
                if (commentIdParam != null) {
                    int commentId = Integer.parseInt(commentIdParam);
                    commentDao.deleteComment(commentId, user.getId());
                }
            } else if ("update".equals(action)) {
                // Xử lý sửa bình luận
                String commentIdParam = request.getParameter("commentId");
                String newContent = request.getParameter("content");

                if (commentIdParam != null && newContent != null && !newContent.trim().isEmpty()) {
                    int commentId = Integer.parseInt(commentIdParam);

                    Comment comment = new Comment();
                    comment.setCommentId(commentId);
                    comment.setContent(newContent.trim());

                    commentDao.updateComment(comment, user.getId());
                }
            }

            // Sau khi xử lý, chuyển hướng lại trang chi tiết sản phẩm
            response.sendRedirect("productdetail?id=" + productId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("productdetail?id=" + productIdParam);
        }
    }
    }

