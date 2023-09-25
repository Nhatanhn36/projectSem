package com.example.productmanager.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "user1")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2")
    private User user2;

    @Column(name = "text")
    private String text;

    @Column(name = "time_chat")
    private Timestamp timeChat;

    @Column(name = "violate")
    private boolean violate;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Column(name = "status")
    private boolean status;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimeChat() {
        return timeChat;
    }

    public void setTimeChat(Timestamp timeChat) {
        this.timeChat = timeChat;
    }

    public boolean isViolate() {
        return violate;
    }

    public void setViolate(boolean violate) {
        this.violate = violate;
    }
// Constructors, getters, setters
}
