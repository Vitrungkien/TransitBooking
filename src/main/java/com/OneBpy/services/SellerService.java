package com.OneBpy.services;

import com.OneBpy.dtos.*;
import com.OneBpy.models.*;
import java.util.List;

public interface SellerService {
    // Thêm sản phẩm
    void addProduct(ProductDTO productDTO);

    //Cập nhật thông tin sản phẩm
    void updateProduct(ProductDTO productDTO, Long product_id);

    List<Stop> getStopList(Long product_id);
    void addStop(StopDTO stop, Long product_id);

    // Cập nhật điểm dừng
    void updateStop(StopDTO stopDTO, Long stop_id);

    Product getProductById(Long product_id);
    Stop getStopById(Long stop_id);
    Boolean deleteStop(Long stop_id);
    Store editStore(EditStore editStore);
    Store creatStore(User seller);

    //Cập nhật trạng thái đơn hàng
    void exceptedOrder(Long order_id, UpdatedOrder updatedOrder);

    //Cập nhật trạng thái hiển thị
    void displayStatus(Long product_id, HideShowProduct hideShowProduct);

    void softRemoveProduct(Long product_id, RemoveProductDTO removeProductDTO);

    void createNotice(CreateNoticeDTO noticeDTO);
    void updateNotice(UpdateNoticeDTO noticeDTO);

    void markStop(List<StopDTO> stopDTOList);


}
