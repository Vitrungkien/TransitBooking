package com.OneBpy.models;

import com.OneBpy.dtos.NoticeDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notice_tb")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;
    private String title;
    private String content;
    private String storeName;
    private boolean expired;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
}
