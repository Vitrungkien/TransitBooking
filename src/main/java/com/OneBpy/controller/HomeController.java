package com.OneBpy.controller;

import com.OneBpy.dtos.PDTO;
import com.OneBpy.dtos.UserDto;
import com.OneBpy.models.Notice;
import com.OneBpy.models.Product;
import com.OneBpy.models.ResponseObject;
import com.OneBpy.repositories.NoticeRepository;
import com.OneBpy.repositories.OrderRepository;
import com.OneBpy.repositories.ProductRepository;
import com.OneBpy.repositories.StoreRepository;
import com.OneBpy.services.OrderDto;
import com.OneBpy.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class HomeController {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final StoreRepository storeRepository;
    private final NoticeRepository noticeRepository;

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @GetMapping("/all-product")
    List<PDTO> getAllProduct(){
        List<Product> productList =  productRepository.findAllActiveProducts();
        return userService.getAllProduct(productList);
    }

    @GetMapping("/my-order/{product_id}")
    public ResponseEntity<ResponseObject> getProductOfOrder(@PathVariable("product_id") Long product_id) {
        return ResponseEntity.ok(
                new ResponseObject("ok", "lay ve da mua thanh cong"
                ,userService.getProductById(product_id))
        );
    }


    @GetMapping("/all-notice")
    public List<Notice> getAllNotice() {
        return noticeRepository.getAllActiveNotice();
    }

    @GetMapping("/all-store-name")
    public List<String> getStoreName() {
        return storeRepository.getAllStorename();
    }

    @PostMapping("/search-by-stop")
    public ResponseEntity<ResponseObject> search(@RequestBody Map<String, String> searchRequest) {

        List<Product> productList = new ArrayList<>();
        if (searchRequest.containsKey("keyword")) {
            String keyword = searchRequest.get("keyword");
            productList = productRepository.findByKeyword(keyword);
        } else if (searchRequest.containsKey("startAddress") && searchRequest.containsKey("endAddress") && searchRequest.containsKey("startTime1") ) {
            LocalTime startTime1 = LocalTime.parse((String)searchRequest.get("startTime1"));
            String startAddress = searchRequest.get("startAddress");
            String endAddress = searchRequest.get("endAddress");
            logger.info("Start time 1: " + startTime1.plusHours(1));
            LocalTime startTime2 = startTime1.plusHours(1);

            productList = productRepository.findProductsByTimeAndAddress(
                    startTime1, startTime2, startAddress, endAddress);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Fail", "Bad request form", "")
            );
        }

        List<PDTO> products = userService.getAllProduct(productList);

        if (products.isEmpty()) {
            return ResponseEntity.ok().body(
                    new ResponseObject("Fail", "Not Found", "")
            );
        }
        return ResponseEntity.ok().body(
                new ResponseObject("ok", "Tim kiem thanh cong", products)
        );
    }

    @PostMapping("/create-admin")
    public ResponseEntity<ResponseObject> createAdmin(@RequestBody UserDto userDto) {
        UserDto response = userService.createAdminAccount(userDto);
        return ResponseEntity.ok(new ResponseObject("200", "ok", response));
    }
}

