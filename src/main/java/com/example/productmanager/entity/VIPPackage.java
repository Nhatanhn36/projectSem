    package com.example.productmanager.entity;

    import jakarta.persistence.*;

    @Entity
    @Table(name = "vip_package")
    public class VIPPackage {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)

        @Column(name = "vip_id")
        public int vipId;

        @Column(name = "name")
        public String name;

        public long getVipId() {
            return vipId;
        }

        public void setVipId(int vipId) {
            this.vipId = vipId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Column(name = "days")
        public long days;
        @Column(name = "percent_off")
        public double percentOff;

        public void setDays(long days) {
            this.days = days;
        }

        public double getPercentOff() {
            return percentOff;
        }

        public void setPercentOff(double percentOff) {
            this.percentOff = percentOff;
        }

        @Column(name = "description")
        public String description;

        @Column(name = "price")
        public double price;

        public VIPPackage() {

        }

    }
