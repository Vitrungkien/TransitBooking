package com.OneBpy.services.impl;

import com.OneBpy.dtos.*;
import com.OneBpy.models.*;
import com.OneBpy.repositories.*;
import com.OneBpy.services.SellerService;
import com.OneBpy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final ProductRepository productRepository;
    private final StopRepository stopRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final NoticeRepository noticeRepository;
    private UserService userService;

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    // Thêm 1 sản phẩm
    @Override
    public void addProduct(ProductDTO productDTO) {
        Product newProduct = new Product();
        putProduct(newProduct, productDTO);
        newProduct.setStore(userService.getCurrentUser().getStore());
        Product product = productRepository.save(newProduct);
        product.setDisplay(false);
        product.setDeleted(false);
        Long productId = product.getProductID();
        List<StopDTO> stopList = productDTO.getStopList();
        if (!stopList.isEmpty()) {
            for (StopDTO stop : stopList) {
                if (!stop.isDeleted()) {
                    addStop(stop, productId);
                }
            }
        }
        productRepository.save(newProduct);
    }

    // Thêm/sửa một sản phẩm
    public void putProduct(Product updateProduct, ProductDTO productDTO) {
        updateProduct.setProductName(productDTO.getProductName());
        updateProduct.setProductImage(productDTO.getProductImage());
        updateProduct.setRemainSeat(productDTO.getRemainSeat());
        updateProduct.setBienSoXe(productDTO.getBienSoXe());
        updateProduct.setPhoneNumber(productDTO.getPhoneNumber());
        updateProduct.setPhoneNumber2(productDTO.getPhoneNumber2());
        updateProduct.setType(productDTO.getType());
        updateProduct.setDescription(productDTO.getDescription());
        updateProduct.setTienIch(productDTO.getTienIch());
        updateProduct.setPolicy(productDTO.getPolicy());
        updateProduct.setPrice(productDTO.getPrice());
        updateProduct.setStartTime(productDTO.getStartTime());
        updateProduct.setEndTime(productDTO.getEndTime());
        updateProduct.setStartAddress(productDTO.getStartAddress());
        updateProduct.setEndAddress(productDTO.getEndAddress());
        updateProduct.setLastUpdate(new Date());
        productRepository.save(updateProduct);
    }

    //Thêm 1 điểm dừng
    @Override
    public void addStop(StopDTO stop, Long productId) {
        if (productId == null) {
            return;
        }
        Stop newStop = new Stop();
        newStop.setStopTime(stop.getStopTime());
        newStop.setStopAddress(stop.getStopAddress());
        newStop.setDeleted(false);
        Product product = getProductById(productId);
        newStop.setProduct(product);
        stopRepository.save(newStop);
    }

    //Cập nhật sản phẩm
    @Override
    public void updateProduct(ProductDTO productDTO, Long product_id) {
        Product updateProduct = getProductById(product_id);
        putProduct(updateProduct, productDTO);
        List<StopDTO> stopList = productDTO.getStopList();
        if (!stopList.isEmpty()) {
            for (StopDTO stop : stopList) {
                Long stopId = stop.getStopID();
                if (!stop.isDeleted() && stopId == -1) {
                    addStop(stop, product_id);
                } else if (stop.isDeleted() && stopId != -1) {
                    deleteStop(stopId);
                } else if (!stop.isDeleted()) {
                    updateStop(stop, stopId);
                }
            }
        }
        productRepository.save(updateProduct);
    }

    // Cập nhật 1 điểm dừng
    @Override
    public void updateStop(StopDTO stopDTO, Long stop_id) {
        Stop updateStop = getStopById(stop_id);
        if (updateStop == null) {
            return;
        }
        updateStop.setStopTime(stopDTO.getStopTime());
        updateStop.setStopAddress(stopDTO.getStopAddress());
        updateStop.setRightNow(stopDTO.isRightNow());
        stopRepository.save(updateStop);
    }

    //Lấy ra danh sach điểm dừng của 1 vé xe by id
    @Override
    public List<Stop> getStopList(Long product_id) {
        Product product = getProductById(product_id);
        return product.getStopList();
    }

    //Lấy ra sản phẩm by id
    @Override
    public Product getProductById(Long product_id) {
        Optional<Product> productOptional = productRepository.findById(product_id);
        return productOptional.orElse(null);
    }

    //Lấy ra 1 điểm dừng by id
    @Override
    public Stop getStopById(Long stop_id) {
        Optional<Stop> stop = stopRepository.findById(stop_id);
        return stop.orElse(null);
    }

    //Xóa 1 điểm dừng
    @Override
    public Boolean deleteStop(Long stop_id) {
        boolean exists = stopRepository.existsById(stop_id);
        if (exists) {
            stopRepository.deleteById(stop_id);
                return true;
        }
        return false;
    }

    //Sửa thông tin cửa hàng
    @Override
    public Store editStore(EditStore editStore) {
        Store editedStore = userService.getCurrentUser().getStore();
        editedStore.setStoreName(editStore.getStoreName());
        editedStore.setIntroduce(editStore.getIntroduce());
        editedStore.setPhoneNumber(editStore.getPhoneNumber());
        return storeRepository.save(editedStore);
    }


    //Tạo mới cửa hàng
    @Override
    public Store creatStore(User newSeller) {
        Store newStore = new Store();
        newStore.setStoreName("Nhà xe " + newSeller.getFirstName() + " " + newSeller.getLastName());
        newStore.setIntroduce("Giới thiệu chuyến xe");
        newStore.setPhoneNumber(newSeller.getPhoneNumber());
        return storeRepository.save(newStore);
    }


    // Cập nhật trạng thái đơn hàng
    @Override
    public void exceptedOrder(Long order_id, UpdatedOrder updatedOrder) {
        Optional<Order> orderOptional = orderRepository.findById(order_id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            String action = updatedOrder.getOrderAction();
            switch (action) {
                case "Hủy" -> order.setOrderStatus("Đã hủy");
                case "Xác nhận" -> {
                    order.setTotalPrice(updatedOrder.getTotalPrice());
                    order.setOrderStatus("Đã xác nhận");
                }
                case "Hoàn thành" -> order.setOrderStatus("Đã hoàn thành");
                default -> order.setOrderStatus("Error");
            }
            order.setLastUpdate(new Date());
            orderRepository.save(order);
        }
    }


    // Ẩn/hiện sản phẩm
    @Override
    public void displayStatus(Long product_id, HideShowProduct hideShowProduct) {
        Product product = getProductById(product_id);
        product.setDisplay(hideShowProduct.isDisplay());
        productRepository.save(product);
    }

    @Override
    public void softRemoveProduct(Long product_id, RemoveProductDTO removeProductDTO) {
        Product product = getProductById(product_id);
        product.setDeleted(removeProductDTO.isDeleted());
        productRepository.save(product);
    }

    @Override
    public void createNotice(CreateNoticeDTO noticeDTO) {
        Notice newNotice = new Notice();
        newNotice.setTitle(noticeDTO.getTitle());
        newNotice.setContent(noticeDTO.getContent());
        newNotice.setExpired(false);
        newNotice.setStoreName(userService.getCurrentUser().getStore().getStoreName());
        newNotice.setLastUpdate(new Date());
        Long productID = noticeDTO.getProductID();
        newNotice.setProduct(getProductById(productID));
        noticeRepository.save(newNotice);
    }

    @Override
    public void updateNotice(UpdateNoticeDTO noticeDTO) {
        Long noticeID = noticeDTO.getNoticeID();
        Optional<Notice> updateNoticeOptional = noticeRepository.findById(noticeID);
        if (updateNoticeOptional.isPresent()) {
            Notice updateNotice = updateNoticeOptional.get();
            updateNotice.setTitle(noticeDTO.getTitle());
            updateNotice.setContent(noticeDTO.getContent());
            updateNotice.setExpired(noticeDTO.isExpired());
            updateNotice.setLastUpdate(new Date());
            noticeRepository.save(updateNotice);
        }
    }

    @Override
    public void markStop(List<StopDTO> stopDTOList) {
        for (StopDTO stop : stopDTOList) {
            Long stopId = stop.getStopID();
            updateStop(stop, stopId);
        }
    }
}
