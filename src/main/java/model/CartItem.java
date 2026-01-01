package model;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }


    // Hàm tính tổng giá trị của CartItem sau giảm giá
    public BigDecimal getTotal() {
        // Tính giá trị sau giảm giá
        BigDecimal discountAmount = product.getPrice().multiply(product.getDiscountPercentage()).divide(new BigDecimal("100"));
        BigDecimal priceAfterDiscount = product.getPrice().subtract(discountAmount);


        // Tính tổng giá trị sau khi áp dụng số lượng
        CartItem cartItem = new CartItem(product, quantity);
        BigDecimal total = priceAfterDiscount.multiply(new BigDecimal(cartItem.getQuantity()));

        return total;
    }
    public static void main(String[] args) {
        // Tạo sản phẩm với tên, giá, và tỷ lệ giảm giá
        Product product = new Product(1,"Laptop","FF",new BigDecimal(10000),2,"LA","ffff",new Timestamp(12/11/2000),new BigDecimal(20));// Giá 1500, giảm 10%

        // Tạo CartItem với sản phẩm trên và số lượng là 2
        CartItem cartItem = new CartItem(product, 2);

        // Tính tổng giá trị sau giảm giá cho sản phẩm này trong giỏ hàng
        BigDecimal totalPrice = cartItem.getTotal();

        // In kết quả
        System.out.println("Total Price for " + cartItem.getQuantity() + " " + product.getPrice() + " priced products: " + totalPrice);
    }
}
