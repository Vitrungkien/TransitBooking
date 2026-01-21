package com.OneBpy.controller;

import com.OneBpy.dtos.PDTO;
import com.OneBpy.dtos.ProductDTO;
import com.OneBpy.models.Notice;
import com.OneBpy.models.Product;
import com.OneBpy.repositories.*;
import com.OneBpy.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final NoticeRepository noticeRepository;
    private final RedisService redisService;
    private final MapperService mapperService;

    @GetMapping("/all-product")
    List<ProductDTO> getAllProduct() {
        try {
            List<ProductDTO> cached = redisService.getList("all-product", ProductDTO.class);
            if (cached != null) {
                return cached;
            }

            List<Product> productList = productService.findAllProducts();
            List<ProductDTO> result = mapperService.toProductDTOList(productList);

            redisService.setList("all-product", result, 100000);
            return result;
        } catch (Exception e) {
            log.error("Lỗi lấy danh sách vé: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/my-orders")
    public List<OrderDto> getUserOrders() {
        Long userId = userService.getCurrentUser().getUserId();
        return orderRepository.getAllUserOrder(userId);
    }

    @GetMapping("/my-order/{product_id}")
    public PDTO getProductOfOrder(@PathVariable("product_id") Long product_id) {
        return userService.getProductById(product_id);
    }

    @GetMapping("/all-notice")
    public List<Notice> getAllNotice() {
        return noticeRepository.getAllActiveNotice();
    }

    @GetMapping("/all-store-name")
    public List<String> getStoreName() {
        return storeRepository.getAllStoreName();
    }

}
