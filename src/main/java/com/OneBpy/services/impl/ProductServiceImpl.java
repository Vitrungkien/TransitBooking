package com.OneBpy.services.impl;

import com.OneBpy.models.*;
import com.OneBpy.repositories.*;
import com.OneBpy.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final NoticeRepository noticeRepository;
    private final StopRepository stopRepository;
    private final StoreRepository storeRepository;

    public ProductServiceImpl(ProductRepository productRepository, OrderRepository orderRepository, NoticeRepository noticeRepository, StopRepository stopRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.noticeRepository = noticeRepository;
        this.stopRepository = stopRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Store findStoreById(Long id) {
        return storeRepository.findById(id).orElse(null);
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Stop findStopById(Long id) {
        return stopRepository.findById(id).orElse(null);
    }

    @Override
    public Notice findNoticeById(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllProductsByUserId(Long userId) {
        return productRepository.findAllProductsByStore_User_UserId(userId);
    }

    @Override
    public List<Order> findAllOrdersByStoreId(Long store_id) {
        return List.of();
    }

    @Override
    public List<Order> findAllOrdersByProductId(Long product_id) {
        return List.of();
    }

    @Override
    public List<Order> findAllOrdersByUserId(Long store_id) {
        return List.of();
    }

    @Override
    public List<Notice> findAllNoticesByStoreId(Long store_id) {
        return List.of();
    }
}
