package com.example.productmanager.service;

import com.example.productmanager.dao.UserInfoRepository;
import com.example.productmanager.entity.UserInfo;
import com.example.productmanager.entity.VIPPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VipService {

    private final UserInfoRepository userInfoRepository;
    private final VipRepository vipRepository;

    @Autowired

    public VipService(UserInfoRepository userInfoRepository, VipRepository vipRepository) {
        this.userInfoRepository = userInfoRepository;
        this.vipRepository = vipRepository;
    }

    @Transactional
    public UserInfo getUserInfoByUserId(Long userId) {
        return userInfoRepository.findByUserUserId(userId);
    }

    @Transactional
    public void saveUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }


    public List<VIPPackage> findAll() {
        return vipRepository.findAll();
    }
}
