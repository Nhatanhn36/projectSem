package com.example.productmanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetails {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orderId;

    @Column(name = "total_amount")
    private double soTienGoc;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Order getOrderId() {
        return orderId;
    }

    public void setOrderId(Order orderId) {
        this.orderId = orderId;
    }

    public double getSoTienGoc() {
        return soTienGoc;
    }

    public void setSoTienGoc(double soTienGoc) {
        this.soTienGoc = soTienGoc;
    }

    public double getSoTienNhan() {
        return soTienNhan;
    }

    public void setSoTienNhan(double soTienNhan) {
        this.soTienNhan = soTienNhan;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Column(name = "amount_received")
    private double soTienNhan;

    @Column(name = "percent")
    private double percent;

    public String getPayment() {
        return paymentMethod;
    }

    public void setPayment(String payment) {
        this.paymentMethod = payment;
    }

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "full_name_order")
    private String tenNguoiNhan;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTenNguoiNhan() {
        return tenNguoiNhan;
    }

    public void setTenNguoiNhan(String tenNguoiNhan) {
        this.tenNguoiNhan = tenNguoiNhan;
    }

    public String getPhoneNguoiNhan() {
        return phoneNguoiNhan;
    }

    public void setPhoneNguoiNhan(String phoneNguoiNhan) {
        this.phoneNguoiNhan = phoneNguoiNhan;
    }

    public String getDiachiNguoiNhan() {
        return diachiNguoiNhan;
    }

    public void setDiachiNguoiNhan(String diachiNguoiNhan) {
        this.diachiNguoiNhan = diachiNguoiNhan;
    }

    @Column(name = "phone_order")
    private String phoneNguoiNhan;

    @Column(name = "address_order")
    private String diachiNguoiNhan;



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
