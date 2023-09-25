package com.example.productmanager.entity;

import com.example.productmanager.dao.UserRepository;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "users")
public class User {


    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    @OneToMany(mappedBy = "user1")
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "user2")
    private List<Message> receivedMessages;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public Long userId;

    @Column(name = "username")
    public String username;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Column(name = "password")
    public String password;

    @Column(name = "is_ban")
    public boolean ban;
    @Column(name = "vip_id")
    public byte vip;
    @Column(name = "coint")
    public double coint;
    @Column(name = "role")
    public byte role;

    public User() {
        this.ban = false;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public byte getVip() {
        return vip;
    }

    public void setVip(byte vip) {
        this.vip = vip;
    }

    public double getCoint() {
        return coint;
    }

    public void setCoint(double coint) {
        this.coint = coint;
    }

    public byte getRole() {
        return role;
    }

    public void setRole(byte role) {
        this.role = role;
    }

    public User(Long userId, String username, String password, boolean ban, byte vip, int coint, byte role, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.ban = ban;
        this.vip = vip;
        this.coint = coint;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(Long userId, String username, String password, byte role, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    @Column(name = "created_at")
    public Timestamp createdAt;


}