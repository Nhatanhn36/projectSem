package com.example.productmanager.dao;

import com.example.productmanager.entity.UserInfo;
import com.example.productmanager.entity.VIPPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VIPPackageRepository extends JpaRepository<VIPPackage, Long> {

}
