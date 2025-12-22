package com.OneBpy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "product_tb")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    private String productName;
    private String productImage;
    private int remainSeat;
    private boolean display;
    private String bienSoXe;
    private String phoneNumber;
    private String phoneNumber2;
    private String description;
    private String policy;
    private String tienIch;
    private String type;
    private int price;
    private LocalTime startTime;
    private LocalTime endTime;
    private String startAddress;
    private String endAddress;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnore
    private Store store;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Stop> stopList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Notice> noticeList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Order> orderList;
}
