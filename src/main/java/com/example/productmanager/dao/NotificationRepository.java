package com.example.productmanager.dao;

import com.example.productmanager.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserUserIdOrderByCreatedAtDesc(Long userId);

}