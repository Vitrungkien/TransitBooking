package com.OneBpy.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StopDTO {
    private Long stopId;
    private String stopAddress;
    private LocalTime stopTime;
    private boolean rightNow;
    private boolean deleted;
    private Long productId;
    private LocalDateTime createdAt;
}
