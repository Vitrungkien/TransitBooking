package com.OneBpy.services;

import com.OneBpy.dtos.*;
import com.OneBpy.models.*;

import java.util.List;

public interface MapperService {
    User toUserEntity(UserDTO userDTO);
    UserDTO toUserDTO(User user);
    Store toStoreEntity(StoreDTO storeDTO);
    StoreDTO toStoreDTO(Store store);

    Product toProductEntity(ProductDTO productDTO);
    ProductDTO toProductDTO(Product product);
    List<ProductDTO> toProductDTOList(List<Product> products);

    Order toOrderEntity(OrderDTO orderDTO);
    OrderDTO toOrderDTO(Order order);
    Notice  toNoticeEntity(NoticeDTO noticeDTO);
    NoticeDTO toNoticeDTO(Notice notice);
    Stop toStopEntity(StopDTO stopDTO);
    StopDTO toStopDTO(Stop stop);
}
