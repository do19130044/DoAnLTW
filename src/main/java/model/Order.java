package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int userId;
    private BigDecimal totalPrice;
    private String shippingAddress;
    private String status;
    private Timestamp orderDate;
    private User customer;

    public Order(int orderId, int userId, BigDecimal totalPrice, String shippingAddress, String status, Timestamp orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.orderDate = orderDate;
    }
    public Order() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
    public User getCustomer() {
        return customer;  // Getter cho thuộc tính customer
    }

    public void setCustomer(User customer) {
        this.customer = customer;  // Setter cho thuộc tính customer
    }
}
