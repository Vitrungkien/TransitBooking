package com.OneBpy.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "product"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "order_tb")
public class Order {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    private String pickUpAddress;
    private String destinationAddress;
    private LocalDateTime pickTime;
    private String message;
    private int quantity;
    private String phoneNumber;
    private int price;
    private int totalPrice;
    private String orderStatus;
    private LocalDateTime lastUpdate;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
