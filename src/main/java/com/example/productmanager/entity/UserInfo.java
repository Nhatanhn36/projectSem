package com.example.productmanager.entity;

import jakarta.persistence.*;


import java.util.Date;

@Entity
@Table(name = "users_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

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

    public String getImageAvatar() {
        return imageAvatar;
    }

    public void setImageAvatar(String imageAvatar) {
        this.imageAvatar = imageAvatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    public User user;

    @Column(name = "image_avatar")
    public String imageAvatar;

    @Column(name = "fullname", nullable = false)
    public String fullname;

    @Column(name = "email", nullable = false)
    public String email;

    @Column(name = "phone")
    public String phone;

    @Column(name = "gender")
    public String gender;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    public String date;

    @Column(name = "address", columnDefinition = "TEXT")
    public String address;

    @Column(name = "updated_time", columnDefinition = "TIMESTAMP DEFAULT current_timestamp() ON UPDATE current_timestamp()")
    public Date updatedTime;

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", user=" + user +
                ", imageAvatar='" + imageAvatar + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
