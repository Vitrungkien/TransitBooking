package com.OneBpy.services.impl;

import com.OneBpy.dtos.*;
import com.OneBpy.models.*;
import com.OneBpy.services.MapperService;
import com.OneBpy.services.ProductService;
import com.OneBpy.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MapperServiceImpl implements MapperService {
    private final UserService userService;
    private final ProductService productService;

    public MapperServiceImpl(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }


    @Override
    public User toUserEntity(UserDTO userDTO) {
        if(userDTO == null){
            return null;
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        Store store = toStoreEntity(userDTO.getStore());
        user.setStore(store);

        for (OrderDTO orderDTO:userDTO.getOrderList()) {
            user.getOrderList().add(toOrderEntity(orderDTO));
        }
        return user;
    }

    @Override
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        Store store = user.getStore();
        userDTO.setStore(toStoreDTO(store));
        for (Order order:user.getOrderList()) {
            userDTO.getOrderList().add(toOrderDTO(order));
        }
        return userDTO;
    }

    @Override
    public Store toStoreEntity(StoreDTO storeDTO) {
        if (storeDTO == null) {
            return null;
        }
        Store store = new Store();
        store.setStoreId(storeDTO.getStoreId());
        store.setStoreName(storeDTO.getStoreName());
        store.setIntroduce(storeDTO.getIntroduce());
        store.setPhoneNumber(storeDTO.getPhoneNumber());
        for (ProductDTO productDTO:storeDTO.getProductList()) {
            store.getProductList().add(toProductEntity(productDTO));
        }
        return store;
    }

    @Override
    public StoreDTO toStoreDTO(Store store) {
        if (store == null) {
            return null;
        }
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreId(store.getStoreId());
        storeDTO.setStoreName(store.getStoreName());
        storeDTO.setIntroduce(store.getIntroduce());
        storeDTO.setPhoneNumber(store.getPhoneNumber());
//        for (Product s:store.getProductList()) {
//            storeDTO.getProductList().add(toProductDTO(s));
//        }
        return storeDTO;
    }

    @Override
    public Product toProductEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setProductName(productDTO.getProductName());
        product.setProductImage(productDTO.getProductImage());
        product.setRemainSeat(productDTO.getRemainSeat());
        product.setDisplay(productDTO.isDisplay());
        product.setBienSoXe(productDTO.getBienSoXe());
        product.setPhoneNumber(productDTO.getPhoneNumber());
        product.setPhoneNumber2(productDTO.getPhoneNumber2());
        product.setDescription(productDTO.getDescription());
        product.setPolicy(productDTO.getPolicy());
        product.setTienIch(productDTO.getTienIch());
        product.setType(productDTO.getType());
        product.setPrice(productDTO.getPrice());
        product.setStartTime(productDTO.getStartTime());
        product.setEndTime(productDTO.getEndTime());
        product.setStartAddress(productDTO.getStartAddress());
        product.setEndAddress(productDTO.getEndAddress());
        product.setDeleted(productDTO.isDeleted());

        for (OrderDTO orderDTO:productDTO.getOrderList()) {
            product.getOrderList().add(toOrderEntity(orderDTO));
        }
        for (NoticeDTO noticeDTO:productDTO.getNoticeList()) {
            product.getNoticeList().add(toNoticeEntity(noticeDTO));
        }
        for (StopDTO stopDTO:productDTO.getStopList()) {
            product.getStopList().add(toStopEntity(stopDTO));
        }
        return product;
    }

    @Override
    public ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductImage(product.getProductImage());
        productDTO.setRemainSeat(product.getRemainSeat());
        productDTO.setDisplay(product.isDisplay());
        productDTO.setBienSoXe(product.getBienSoXe());
        productDTO.setPhoneNumber(product.getPhoneNumber());
        productDTO.setPhoneNumber2(product.getPhoneNumber2());
        productDTO.setDescription(product.getDescription());
        productDTO.setPolicy(product.getPolicy());
        productDTO.setTienIch(product.getTienIch());
        productDTO.setType(product.getType());
        productDTO.setStartTime(product.getStartTime());
        productDTO.setEndTime(product.getEndTime());
        productDTO.setStartAddress(product.getStartAddress());
        productDTO.setEndAddress(product.getEndAddress());
        productDTO.setDeleted(product.isDeleted());
        productDTO.setCreatedAt(product.getCreatedAt());
        productDTO.setLastUpdate(product.getLastUpdate());

        productDTO.setStore(toStoreDTO(product.getStore()));

        productDTO.setOrderList(new ArrayList<>());
        for (Order orderDTO:product.getOrderList()) {
            productDTO.getOrderList().add(toOrderDTO(orderDTO));
        }

        productDTO.setNoticeList(new ArrayList<>());
        for (Notice notice:product.getNoticeList()) {
            productDTO.getNoticeList().add(toNoticeDTO(notice));
        }

        productDTO.setStopList(new ArrayList<>());
        for (Stop stop:product.getStopList()) {
            productDTO.getStopList().add(toStopDTO(stop));
        }
        return productDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> toProductDTOList(List<Product> products) {
        if (products == null) {
            return List.of();
        }
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product:products) {
            productDTOList.add(toProductDTO(product));
        }
        return productDTOList;
    }

    @Override
    public Order toOrderEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setPickUpAddress(orderDTO.getPickUpAddress());
        order.setDestinationAddress(orderDTO.getDestinationAddress());
        order.setPickTime(orderDTO.getPickTime());
        order.setMessage(orderDTO.getMessage());
        order.setQuantity(orderDTO.getQuantity());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setPrice(orderDTO.getPrice());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setOrderStatus(orderDTO.getOrderStatus());

        order.setUser(userService.findUserById(orderDTO.getUserId()));
        order.setProduct(productService.findProductById(orderDTO.getProductId()));
        return order;
    }

    @Override
    public OrderDTO toOrderDTO(Order order) {
        if (order == null) {
            return null;
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setPickUpAddress(order.getPickUpAddress());
        orderDTO.setDestinationAddress(order.getDestinationAddress());
        orderDTO.setPickTime(order.getPickTime());
        orderDTO.setMessage(order.getMessage());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setPhoneNumber(order.getPhoneNumber());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setLastUpdate(order.getLastUpdate());

        orderDTO.setUserId(order.getUser().getUserId());
        orderDTO.setProductId(order.getProduct().getProductId());
        return orderDTO;
    }

    @Override
    public Notice toNoticeEntity(NoticeDTO noticeDTO) {
        if (noticeDTO == null) {
            return null;
        }
        Notice notice = new Notice();
        notice.setNoticeId(noticeDTO.getNoticeId());
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setStoreName(noticeDTO.getStoreName());
        notice.setExpired(noticeDTO.isExpired());
        notice.setProduct(productService.findProductById(noticeDTO.getProductId()));
        return notice;
    }

    @Override
    public NoticeDTO toNoticeDTO(Notice notice) {
        if (notice == null) {
            return null;
        }
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setNoticeId(notice.getNoticeId());
        noticeDTO.setTitle(notice.getTitle());
        noticeDTO.setContent(notice.getContent());
        noticeDTO.setStoreName(notice.getStoreName());
        noticeDTO.setExpired(notice.isExpired());
        noticeDTO.setProductId(notice.getProduct().getProductId());
        noticeDTO.setCreatedAt(notice.getCreatedAt());
        noticeDTO.setLastUpdate(notice.getLastUpdate());
        return noticeDTO;
    }

    @Override
    public Stop toStopEntity(StopDTO stopDTO) {
        if (stopDTO == null) {
            return null;
        }
        Stop stop = new Stop();
        stop.setStopId(stopDTO.getStopId());
        stop.setStopTime(stopDTO.getStopTime());
        stop.setStopAddress(stopDTO.getStopAddress());
        stop.setRightNow(stopDTO.isRightNow());
        stop.setDeleted(stopDTO.isDeleted());
        stop.setProduct(productService.findProductById(stopDTO.getProductId()));
        return stop;
    }

    @Override
    public StopDTO toStopDTO(Stop stop) {
        if (stop == null) {
            return null;
        }
        StopDTO stopDTO = new StopDTO();
        stopDTO.setStopId(stop.getStopId());
        stopDTO.setStopTime(stop.getStopTime());
        stopDTO.setStopAddress(stop.getStopAddress());
        stopDTO.setRightNow(stop.isRightNow());
        stopDTO.setDeleted(stop.isDeleted());
        stopDTO.setProductId(stop.getProduct().getProductId());
        stopDTO.setCreatedAt(stop.getCreatedAt());
        return stopDTO;
    }
}
