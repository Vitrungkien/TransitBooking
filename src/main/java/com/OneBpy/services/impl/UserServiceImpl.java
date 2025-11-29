package com.OneBpy.services.impl;

import com.OneBpy.dtos.OrderRequest;
import com.OneBpy.dtos.PDTO;
import com.OneBpy.models.*;
import com.OneBpy.repositories.OrderRepository;
import com.OneBpy.repositories.ProductRepository;
import com.OneBpy.repositories.UserRepository;
import com.OneBpy.services.SellerService;
import com.OneBpy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private SellerService sellerService;

    @Autowired
    public void setSellerService(@Lazy SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + email));
    }

    @Override
    public User editProfile(User updateUser) {
        // Lấy thông tin người dùng đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Cập nhật thông tin người dùng
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        User newUser;
        if (user.isPresent()) {
            newUser = user.get();
            newUser.setFirstName(updateUser.getFirstName());
            newUser.setLastName(updateUser.getLastName());
            newUser.setPhoneNumber(updateUser.getPhoneNumber());

            //Lưu người dùng vào cơ sở dữ liệu
            return userRepository.save(newUser);
        }
        return null;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        return user.orElse(null);
    }

    @Override
    public List<Order> getCart() {
        User user = getCurrentUser();
        return user.getOrderList();
    }

    @Override
    public User getUserById(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        return user.orElse(null);
    }

    @Override
    public void createOrder(Long product_id, OrderRequest orderRequest) {
        Order newOrder = new Order();
        newOrder.setPickUpAddress(orderRequest.getPickUpAddress());
        newOrder.setDestinationAddress(orderRequest.getDestinationAddress());
        newOrder.setPickTime(orderRequest.getPickTime());
        newOrder.setMessage(orderRequest.getMessage());
        newOrder.setQuantity(orderRequest.getQuantity());
        newOrder.setPhoneNumber(orderRequest.getPhoneNumber());
        newOrder.setOrderStatus("Chờ xác nhận");
        newOrder.setUser(getCurrentUser());
        Product product = sellerService.getProductById(product_id);
        int price = product.getPrice();
        newOrder.setPrice(price);
        newOrder.setTotalPrice(price * orderRequest.getQuantity());
        newOrder.setProduct(product);
        int remainSeat = product.getRemainSeat();
        remainSeat -= orderRequest.getQuantity();
        product.setRemainSeat(remainSeat);
        productRepository.save(product);
        orderRepository.save(newOrder);
    }

    @Override
    public List<PDTO> getAllProduct(List<Product> productList) {

        List<PDTO> pdtos = new ArrayList<>();
        for (Product product : productList) {
            pdtos.add(productToPDTO(product));
        }
        return pdtos;
    }

    @Override
    public PDTO getProductById(Long product_id) {
        Optional<Product> product = productRepository.findById(product_id);
        return productToPDTO(product.orElse(null));
    }

    public PDTO productToPDTO(Product product) {
        if (product == null) {
            return null;
        }
        Long productID = product.getProductID();
        String productName = product.getProductName();
        String productImage = product.getProductImage();
        int remainSeat = product.getRemainSeat();
        boolean display = product.isDisplay();
        String bienSoXe = product.getBienSoXe();
        String phoneNumber = product.getPhoneNumber();
        String phoneNumber2 = product.getPhoneNumber2();
        String description = product.getDescription();
        String policy = product.getPolicy();
        String tienIch = product.getTienIch();
        String type = product.getType();
        int price = product.getPrice();
        LocalTime startTime = product.getStartTime();
        LocalTime endTime = product.getEndTime();
        String startAddress = product.getStartAddress();
        String endAddress = product.getEndAddress();
        boolean deleted = product.isDeleted();
        Date lastUpdate = product.getLastUpdate();
        Date createdAt = product.getCreatedAt();
        List<Stop> stopList = product.getStopList();
        List<Notice> noticeList = product.getNoticeList();
        List<Order> orderList = product.getOrderList();
        String storeName = product.getStore().getStoreName();
        return new PDTO(productID, productName, productImage, remainSeat, display,
                bienSoXe, phoneNumber, phoneNumber2, description, policy, tienIch, type, price,
                startTime, endTime, startAddress, endAddress, deleted, lastUpdate, createdAt, stopList,
                noticeList, orderList, storeName);
    }


}
