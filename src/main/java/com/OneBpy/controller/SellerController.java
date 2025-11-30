package com.OneBpy.controller;

//import ch.qos.logback.core.model.Model;
import com.OneBpy.dtos.*;
import com.OneBpy.repositories.*;
import com.OneBpy.dtos.StopDtoList;
import com.OneBpy.services.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/management")
@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
public class SellerController {
    private final SellerService sellerService;

    // Xac nhan don hang
    @PostMapping("/my-store/{order_id}/except")
    public String except(@PathVariable("order_id") Long order_id, @ModelAttribute UpdatedOrder updatedOrder) {
        sellerService.exceptedOrder(order_id, updatedOrder);
        return "redirect:/management-order";
    }

    //    Thêm sản phẩm
    @PostMapping("/my-store/add-product")
    public String addProduct(@ModelAttribute ProductDTO productDTO) {
        sellerService.addProduct(productDTO);
        return "redirect:/management";
    }

    //Cập nhật sản phẩm
    @PostMapping("/my-store/{product_id}/update-product")
    public String updateProduct(@PathVariable("product_id") Long product_id,
                                @ModelAttribute ProductDTO productDTO, Model model) {
        try {
            sellerService.updateProduct(productDTO, product_id);
            return "redirect:/management";
        }
        catch (Exception ex) {
            log.error("Lỗi thêm sản phẩm: {}", ex.getMessage(), ex);
            model.addAttribute("error", "Lỗi thêm sản phẩm");
            return "redirect:/management";
        }
    }

    // Ẩn hiện sản phẩm
    @PostMapping("/{product_id}/display-status")
    public String hideP(@PathVariable("product_id") Long product_id,
                        @ModelAttribute HideShowProduct hideShowProduct) {
        sellerService.displayStatus(product_id, hideShowProduct);
        return "redirect:/management";
    }

    //Xóa mềm một sản phẩm
    @PostMapping("/{product_id}/remove")
    public String softRemoveProduct(@PathVariable("product_id") Long product_id,
                                                     @ModelAttribute RemoveProductDTO removeProductDTO) {
        sellerService.softRemoveProduct(product_id, removeProductDTO);
        return "redirect:/management";
    }

    //Thêm thông báo
    @PostMapping("/my-store/create-notice")
    public String createNotice(@ModelAttribute CreateNoticeDTO noticeDTO) {
        sellerService.createNotice(noticeDTO);
        return "redirect:/management-notice";
    }

    // Cập nhật thông báo
    @PostMapping("/my-store/update-notice")
    public String updateNotice(@ModelAttribute UpdateNoticeDTO noticeDTO) {
        sellerService.updateNotice(noticeDTO);
        return "redirect:/management-notice";
    }


    // Chia sẻ vị trí hiện tại
    @PostMapping("/my-store/mark-stop")
    public String markStop(@ModelAttribute StopDtoList stopDtoList) {
        sellerService.markStop(stopDtoList.getStopDTOList());
        return "redirect:/management";
    }

}