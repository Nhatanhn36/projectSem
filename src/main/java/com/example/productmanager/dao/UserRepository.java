package com.example.productmanager.dao;

import com.example.productmanager.entity.Message;
import com.example.productmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    List<User> findByRole(byte role);

    List<User> findDistinctSellersByUserId(Long userId);

    @Query("SELECT DISTINCT ci.product.seller " +
            "FROM CartItem ci " +
            "WHERE ci.user.userId = :userId")
    List<User> findDistinctSellersByCartItemsUser(@Param("userId") Long userId);


    @Query("SELECT u.coint FROM User u WHERE u.userId = :userId")
    int findCointById(Long userId);

    @Query("SELECT u.vip FROM User u WHERE u.userId = :userId")
    int findVipIdById(Long userId);

    @Query("SELECT u.user2.userId FROM Message u WHERE u.user1 = :userId")
    User findUser2UserIdByUser1Id(Long userId);

    @Query("SELECT u.role FROM User u WHERE u.userId = :userId")
    int findRoleByUserId(Long userId);

}

