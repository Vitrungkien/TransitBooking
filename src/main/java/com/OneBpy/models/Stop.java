package com.OneBpy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;
import java.sql.Time;

@Data
@Entity
@Table(name = "stop_tb")
public class Stop {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stop_id")
    private Long stopID;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime stopTime;
    private String stopAddress;
    private boolean rightNow;
    private boolean deleted;
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference("product-stop")
    private Product product;

    @PrePersist
    protected void onCreate() {
        createdAt = new java.util.Date();
    }

//    public boolean getDeleted() {
//        return this.deleted;
//    }
}
