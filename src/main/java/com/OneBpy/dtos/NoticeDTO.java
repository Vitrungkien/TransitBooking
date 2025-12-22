package com.OneBpy.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

    @Getter
    @Setter
    @AllArgsConstructor
    public class NoticeDTO {
        private Long noticeId;
        private Long productId;
        private String storeName;
        private String productName;
        private Double price;
        private LocalTime startTime;
        private String bienSoXe;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private boolean isExpired;
        private LocalDateTime lastUpdate;

    public NoticeDTO() {
    }

    public NoticeDTO(Long noticeId, String title, String content, LocalDateTime createdAt, String storeName, boolean isExpired, LocalDateTime lastUpdate) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.storeName = storeName;
        this.isExpired = isExpired;
        this.lastUpdate = lastUpdate;
    }

    public NoticeDTO(Long noticeId, LocalDateTime createdAt, String title, String content, boolean isExpired,
                     String productName, Long productId, String bienSoXe, LocalTime startTime) {
        this.noticeId = noticeId;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
        this.isExpired = isExpired;
        this.productName = productName;
        this.productId = productId;
        this.startTime = startTime;
        this.bienSoXe = bienSoXe;
    }
}
