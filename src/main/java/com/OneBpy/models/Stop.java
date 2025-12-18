package com.OneBpy.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "product")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "stop_tb")
public class Stop {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stop_id")
    private Long stopId;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime stopTime;
    private String stopAddress;
    private boolean rightNow;
    private boolean deleted;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
