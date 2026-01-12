package adminServlet;

import adminDao.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/deleteProduct")
public class DeleteProductControl extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain; charset=UTF-8");

        String idRaw = request.getParameter("id");

        if (idRaw == null || idRaw.isEmpty()) {
            response.getWriter().write("ID không hợp lệ");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            ProductDao dao = new ProductDao();

            boolean success = dao.deleteProduct(id);

            if (success) {
                response.getWriter().write("Xóa sản phẩm thành công");
            } else {
                response.getWriter().write("Không tìm thấy sản phẩm để xóa");
            }

        } catch (NumberFormatException e) {
            response.getWriter().write("ID không hợp lệ");
        }
    }
}
