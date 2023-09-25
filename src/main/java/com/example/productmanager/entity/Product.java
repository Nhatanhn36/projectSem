package com.example.productmanager.entity;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.sql.Timestamp;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    public long productId;

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantityStock(int quantityStock) {
        this.quantityStock = quantityStock;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public void setHideTrue(boolean hide){
        this.hide = hide;
    }
    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    @Column(name = "name")
    public String name;

    public long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityStock() {
        return quantityStock;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public User getSeller() {
        return seller;
    }

    public Boolean getHide() {
        return hide;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Column(name = "images")
    public String images;

    @Column(name = "description")
    public String description;

    @Column(name = "price")
    public double price;

    @Column(name = "quantity_stock")
    public int quantityStock;

    @Column(name = "sold")
    public int sold;

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    @Column(name = "created_at")
    public Timestamp createdAt;

    @Column(name = "updated_at")
    public Timestamp updatedAt;



    @Column(name = "category_id")
    public Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


    public Product() {
        this.hide = false;
    }
    @ManyToOne
    @JoinColumn(name = "seller_id")
    public User seller;

    @Column(name = "hide")
    public Boolean hide;

}
