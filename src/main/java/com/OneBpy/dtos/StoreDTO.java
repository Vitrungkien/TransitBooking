package com.OneBpy.dtos;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StoreDTO {
    private Long storeId;
    private String storeName;
    private String phoneNumber;
    private String introduce;
    private Date createdAt;
    private Long userId;
    private List<ProductDTO> productList;
}
