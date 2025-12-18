package com.OneBpy.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
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
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private Long userId;
    private Long productId;
}
