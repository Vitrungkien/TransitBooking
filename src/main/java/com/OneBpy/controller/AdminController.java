package com.OneBpy.controller;

import com.OneBpy.models.*;
import com.OneBpy.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
//@CrossOrigin("*")
public class AdminController {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final StopRepository stopRepository;
    private final NoticeRepository noticeRepository;
    private final OrderRepository orderRepository;

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hi Boss");
    }

    @GetMapping("/all-users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/all-store")
    List<Store> getAllStore(){
        return storeRepository.findAll();
    }

    @GetMapping("/all-product")
    List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    @GetMapping("/all-order")
    List<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    @GetMapping("/all-stop")
    List<Stop> getAllStop(){
        return stopRepository.findAll();
    }

    @GetMapping("/all-notice")
    List<Notice> getAllNotice(){
        return noticeRepository.findAll();
    }

}
