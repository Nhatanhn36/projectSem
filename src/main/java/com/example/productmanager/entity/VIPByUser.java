package com.example.productmanager.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "vip_by_user")
public class VIPByUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "vip_id")
    private VIPPackage vipPackage;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "create_time")
    private Timestamp createTime; // thời gian đăng kí
    @Column(name = "end_time")
    private Timestamp endTime; // đã xóa cột vip_time_left,thay thành thời gian hết hạn

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VIPPackage getVipPackage() {
        return vipPackage;
    }

    public void setVipPackage(VIPPackage vipPackage) {
        this.vipPackage = vipPackage;
    }

    public Timestamp getEndTime() {
        return endTime;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "status")
    private String status;

}

