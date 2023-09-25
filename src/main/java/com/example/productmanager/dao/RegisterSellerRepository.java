package com.example.productmanager.dao;

import com.example.productmanager.entity.RegisterSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterSellerRepository extends JpaRepository<RegisterSeller, Long> {
    boolean existsByUserUserId(Long userId);
    RegisterSeller getSellerRequestById(Long id);

    List<RegisterSeller> findAllByUserUserId(Long userId);

    boolean existsByUserUserIdAndStatus(Long userId, String pending);

    List<RegisterSeller> findAllByStatusAndUserUserId(String status, Long userId);

    int countAllByStatusAndUserUserId(String status, Long userId);

    int countAllByStatus(String status);
}

