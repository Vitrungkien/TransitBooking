package com.OneBpy.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "productList"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Product> productList;
}
