package com.example.productmanager.dao;

import com.example.productmanager.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByUserUserId(Long userId);

    @Query("SELECT u.imageAvatar FROM UserInfo u WHERE u.user.userId = :userId")
    String findImageAvatarById(Long userId);
}
