package com.OneBpy.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class NoticeDTO {
    private Long noticeId;
    private Long productId;
    private String productName;
    private Double price;
    private LocalTime startTime;
    private String bienSoXe;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private boolean isExpired;

    public NoticeDTO() {
    }

    public NoticeDTO(Long noticeId, Long productId, String productName, Double price, LocalTime startTime, String bienSoXe, String title, String content, LocalDateTime createdAt, boolean isExpired) {
        this.noticeId = noticeId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.startTime = startTime;
        this.bienSoXe = bienSoXe;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.isExpired = isExpired;
    }
}
