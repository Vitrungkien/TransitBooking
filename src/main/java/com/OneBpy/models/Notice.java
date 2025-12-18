package com.OneBpy.models;

import com.OneBpy.dtos.NoticeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "product")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
    private Product product;
}
