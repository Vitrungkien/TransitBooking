package com.OneBpy.services;

import com.OneBpy.models.*;

import java.util.List;

public interface ProductService {
    Product findProductById(Long id);
    Store findStoreById(Long id);
    Order findOrderById(Long id);
    Stop findStopById(Long id);
    Notice findNoticeById(Long id);

    List<Product> findAllProducts();
    List<Product> findAllProductsByUserId(Long userId);

    List<Order> findAllOrdersByStoreId(Long store_id);
    List<Order> findAllOrdersByProductId(Long product_id);
    List<Order> findAllOrdersByUserId(Long store_id);

    List<Notice> findAllNoticesByStoreId(Long store_id);
}
