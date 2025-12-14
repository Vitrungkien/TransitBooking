package com.OneBpy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "order_tb")
public class Order {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderID;
    private String pickUpAddress;
    private String destinationAddress;
    private LocalDateTime pickTime;
    private String message;
    private int quantity;
    private String phoneNumber;
    private int price;
    private int totalPrice;
    private String orderStatus;
    private Date lastUpdate;
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference  // Đánh dấu mối quan hệ không quản lý
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference("product-order")
    private Product product;

    @PrePersist
    protected void onCreate() {
        createdAt = new java.util.Date();
    }
}
