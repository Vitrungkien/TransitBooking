package com.OneBpy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "store_tb")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;
    private String storeName;
    @Column(nullable = false)
    private String phoneNumber;
    private String introduce;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Product> productList;
}
