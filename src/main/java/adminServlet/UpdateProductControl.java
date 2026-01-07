package adminServlet;

import adminDao.ProductDao;
import model.Product;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.ResourceBundle;

@MultipartConfig(
        location = "D:/LT_FE/ESC/tmp", // Thư mục tạm lưu trữ file, có thể thay đổi
        maxFileSize = 10485760, // 10MB, dung lượng tối đa cho file tải lên
        maxRequestSize = 20971520, // 20MB, dung lượng tối đa cho toàn bộ request
        fileSizeThreshold = 2097152 // 2MB, dung lượng tối thiểu để chuyển file vào bộ nhớ
)
@WebServlet("/admin/updateProduct")
public class UpdateProductControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy productId từ request
        String productIdStr = request.getParameter("productId");
        if (productIdStr == null || productIdStr.isEmpty()) {
            response.getWriter().write("Product ID is missing.");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid product ID.");
            return;
        }

        // Truy vấn thông tin sản phẩm từ cơ sở dữ liệu
        ProductDao productDao = new ProductDao();
        Product product = productDao.getProductById(productId);

        if (product == null) {
            response.getWriter().write("Product not found.");
            return;
        }

        // Chuyển sản phẩm vào request và forward đến JSP
        request.setAttribute("product", product);
        request.getRequestDispatcher("/admin/updateProduct.jsp").forward(request, response);
    }


//    protected void doPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // Lấy thông tin từ form
//        String productIdStr = request.getParameter("productId");
//        if (productIdStr == null || productIdStr.isEmpty()) {
//            response.getWriter().write("Product ID is missing or invalid.");
//            return;
//        }
//        int productId = Integer.parseInt(productIdStr);  // Kiểm tra null và trống trước khi chuyển đổi
//
//        String name = request.getParameter("name");
//        String description = request.getParameter("description");
//
//        String priceStr = request.getParameter("price");
//        BigDecimal price = null;
//        if (priceStr != null && !priceStr.isEmpty()) {
//            price = new BigDecimal(priceStr);  // Nếu giá trị hợp lệ, chuyển thành BigDecimal
//        } else {
//            response.getWriter().write("Price is required.");
//            return;
//        }
//
//        String stockStr = request.getParameter("stock");
//        int stock = 0;
//        if (stockStr != null && !stockStr.isEmpty()) {
//            stock = Integer.parseInt(stockStr);  // Chuyển đổi stock nếu giá trị hợp lệ
//        }
//
//        String category = request.getParameter("category");
//        String discountPercentageStr = request.getParameter("discountPercentage");
//        BigDecimal discountPercentage = null;
//        if (discountPercentageStr != null && !discountPercentageStr.isEmpty()) {
//            discountPercentage = new BigDecimal(discountPercentageStr);
//        }
//
//        // Kiểm tra file ảnh nếu có
//        Part filePart = request.getPart("image");
//        String imagePath = null;
//        if (filePart != null && filePart.getSize() > 0) {
//            String fileName = filePart.getSubmittedFileName();
//            imagePath = "img/" + fileName;
//            String uploadPath = "D:/LT_FE/ESC/src/main/webapp/" + imagePath;
//
//            try (InputStream inputStream = filePart.getInputStream();
//                 OutputStream outputStream = new FileOutputStream(uploadPath)) {
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            }
//        }
//        String oldImg = request.getParameter("oldImg");
//


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        request.setCharacterEncoding("UTF-8");
        //ResourceBundle messages = ResourceBundle.getBundle("messages", Locale.getDefault());
        // Lấy ngôn ngữ từ application scope
        String lang = (String) getServletContext().getAttribute("lang");

        if (lang == null) {
            lang = "en";  // Mặc định là tiếng Anh nếu không có ngôn ngữ trong application scope
        }
        // Đặt locale theo ngôn ngữ người dùng chọn
        Locale locale = new Locale(lang);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

        if (!ServletFileUpload.isMultipartContent(request)) {
            response.getWriter().write("Form must be multipart/form-data");
            return;
        }
        // Lấy thông tin từ form
        String productIdStr = request.getParameter("productId");
        if (productIdStr == null || productIdStr.isEmpty()) {
            response.getWriter().write("Product ID is missing or invalid.");
            return;
        }
        int productId = Integer.parseInt(productIdStr);  // Kiểm tra null và trống trước khi chuyển đổi

        String name = request.getParameter("name");
        String description = request.getParameter("description");

        String priceStr = request.getParameter("price");
        BigDecimal price = null;
        if (priceStr != null && !priceStr.isEmpty()) {
            price = new BigDecimal(priceStr);  // Nếu giá trị hợp lệ, chuyển thành BigDecimal
        } else {
            response.getWriter().write("Price is required.");
            return;
        }

        String stockStr = request.getParameter("stock");
        int stock = 0;
        if (stockStr != null && !stockStr.isEmpty()) {
            stock = Integer.parseInt(stockStr);  // Chuyển đổi stock nếu giá trị hợp lệ
        }

        String category = request.getParameter("category");
        String discountPercentageStr = request.getParameter("discountPercentage");
        BigDecimal discountPercentage = null;
        if (discountPercentageStr != null && !discountPercentageStr.isEmpty()) {
            discountPercentage = new BigDecimal(discountPercentageStr);
        }

        // Kiểm tra file ảnh nếu có
        Part filePart = request.getPart("image");
        String imagePath = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            imagePath = "img/" + fileName;
            String uploadPath = "D:/LT_FE/ESC/src/main/webapp/" + imagePath;

            try (InputStream inputStream = filePart.getInputStream();
                 OutputStream outputStream = new FileOutputStream(uploadPath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
        String oldImg = request.getParameter("oldImg");

        // Tạo đối tượng Product mới để cập nhật
        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setDiscountPercentage(discountPercentage);

        if (imagePath != null) {
            product.setImagePath(filePart.getSubmittedFileName());  // Nếu có ảnh mới thì set lại
        }else {
            product.setImagePath(oldImg);
        }

        // Cập nhật sản phẩm trong cơ sở dữ liệu
        ProductDao productDao = new ProductDao();
        try {
            String updateSuccess = messages.getString("update.success");
            String updateError = messages.getString("update.error");


            boolean isAdded = productDao.updateProduct(product);  // Giả sử bạn đã có phương thức này trong ProductDao
            if (isAdded) {
                response.getWriter().write(updateSuccess);
            } else {
                response.getWriter().write(updateError);
            }
        } catch (Exception e) {
            response.getWriter().write("Có lỗi xảy ra: " + e.getMessage());
            throw new ServletException("Error adding product", e);
        }
    }
}

