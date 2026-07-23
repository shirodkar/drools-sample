package com.example.drools.model;

public class Order {

    private double totalAmount;
    private int itemCount;
    private CustomerType customerType;
    private String couponCode;

    public Order() {
    }

    public Order(double totalAmount, int itemCount, CustomerType customerType, String couponCode) {
        this.totalAmount = totalAmount;
        this.itemCount = itemCount;
        this.customerType = customerType;
        this.couponCode = couponCode;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
