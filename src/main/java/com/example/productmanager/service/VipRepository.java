package com.example.productmanager.service;

import com.example.productmanager.entity.User;
import com.example.productmanager.entity.VIPByUser;
import com.example.productmanager.entity.VIPPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VipRepository extends JpaRepository<VIPPackage, Long> {

}
