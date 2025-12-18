package com.OneBpy.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private StoreDTO store;
    private List<OrderDTO> orderList;
}
