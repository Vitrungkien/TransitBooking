package com.OneBpy.models;

import com.OneBpy.dtos.NoticeDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
@Table(name = "notice_tb")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeID;
    private String title;
    private String content;
    private Date createdAt;
    private String storeName;
    private boolean expired;
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference("product-notice")
    private Product product;

    @PrePersist
    protected void onCreate() {
        createdAt = new java.util.Date();
    }

    public NoticeDTO toNoticeDTO() {
        NoticeDTO noticeDTO = new NoticeDTO();
    }

}
