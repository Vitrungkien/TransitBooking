package com.OneBpy.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"stopList", "noticeList", "orderList"})
@EqualsAndHashCode(exclude = {"stopList", "noticeList", "orderList"})
public class ProductDTO {
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
    private LocalDateTime lastUpdate;
    private LocalDateTime createdAt;
    private Long storeId;
    private List<StopDTO> stopList;
    private List<NoticeDTO> noticeList;
    private List<OrderDTO> orderList;
}
