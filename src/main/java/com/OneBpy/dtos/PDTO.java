package com.OneBpy.dtos;

import com.OneBpy.models.Notice;
import com.OneBpy.models.Order;
import com.OneBpy.models.Stop;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class PDTO {
    private Long productID;
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
    private Date lastUpdate;
    private Date createdAt;
    private List<StopDTO> stopList;
    private List<NoticeDTO> noticeList;
    private List<OrderDTO> orderList;
    private String storeName;

    public PDTO() {}
}
