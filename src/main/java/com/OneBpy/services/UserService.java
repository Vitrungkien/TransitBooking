package com.OneBpy.services;

import com.OneBpy.dtos.OrderRequest;
import com.OneBpy.dtos.PDTO;
import com.OneBpy.dtos.UserDto;
import com.OneBpy.models.Order;
import com.OneBpy.models.Product;
import com.OneBpy.models.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();

    User editProfile(User user);

    User getCurrentUser();

    List<Order> getCart();

    User getUserById(Long user_id);

    Order createOrder(Long product_id, OrderRequest orderRequest);

    List<PDTO> getAllProduct(List<Product> productList);
    PDTO getProductById(Long product_id);
    UserDto createAdminAccount(UserDto userDto);
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
