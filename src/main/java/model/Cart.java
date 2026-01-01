package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int id; // ID giỏ hàng, sẽ được lưu vào cơ sở dữ liệu
    private List<CartItem> items; // Danh sách các sản phẩm trong giỏ hàng

    // Constructor
    public Cart() {
        items = new ArrayList<>();
    }

    public Cart(int id, List<CartItem> items) {
        this.id = id;
        this.items = items;
    }

    // Getter và Setter cho id giỏ hàng
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter cho danh sách sản phẩm trong giỏ hàng
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

//    // Phương thức thêm sản phẩm vào giỏ
//    public void addToCart(Product product, int quantity) {
//        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
//        for (CartItem item : items) {
//            if (item.getProduct().getProductId() == product.getProductId()) {
//                item.setQuantity(item.getQuantity() + quantity);
//                return;
//            }
//        }
//        // Nếu chưa có sản phẩm trong giỏ, thêm mới
//        items.add(new CartItem(product, quantity));
//    }
//
//    // Phương thức xóa sản phẩm khỏi giỏ hàng
//    public void removeFromCart(int productId) {
//        items.removeIf(item -> item.getProduct().getProductId() == productId);
//    }

    // Kiểm tra giỏ hàng có trống không
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalItems() {
        int totalItems = 0;
        for (CartItem item : items) {
            totalItems += item.getQuantity();
        }
        return totalItems;
    }

    // Phương thức tính tổng tiền giỏ hàng (bao gồm giảm giá)
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO; // Khởi tạo BigDecimal với giá trị 0
        for (CartItem item : items) {
            BigDecimal price = item.getProduct().getPrice();
            BigDecimal discountPercentage = item.getProduct().getDiscountPercentage();
            BigDecimal discount = price.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
            BigDecimal priceAfterDiscount = price.subtract(discount);

            // Tính tổng tiền của một sản phẩm trong giỏ hàng
            BigDecimal itemTotal = priceAfterDiscount.multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        return total.setScale(2, BigDecimal.ROUND_HALF_UP); // Đảm bảo kết quả có 2 chữ số thập phân
    }
}
