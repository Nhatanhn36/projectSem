package com.example.productmanager.dao;

import com.example.productmanager.entity.VIPByUser;
import com.example.productmanager.entity.VIPPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface VIPByUserRepository extends JpaRepository<VIPByUser, Long> {

    List<VIPByUser> findVipTimeLeftByUserUserId(Long userId);

    Optional<VIPByUser> findByUserUserId(Long userId);

    void removeByUserUserId(VIPByUser vipByUser);
}

