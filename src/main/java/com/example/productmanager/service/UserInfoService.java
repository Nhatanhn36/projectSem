package com.example.productmanager.service;

import com.example.productmanager.dao.UserInfoRepository;
import com.example.productmanager.entity.User;
import com.example.productmanager.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Transactional
    public UserInfo getUserInfoByUserId(Long userId) {
        return userInfoRepository.findByUserUserId(userId);
    }

    @Transactional
    public void saveUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }
}
