package com.OneBpy.controller;

import com.OneBpy.dtos.OrderRequest;
import com.OneBpy.models.Product;
import com.OneBpy.models.ResponseObject;
import com.OneBpy.repositories.ProductRepository;
import com.OneBpy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final ProductRepository productRepository;

    // Dat hang (active)
    @PostMapping("/{product_id}/order")
    public String creatOrder(@PathVariable("product_id") Long product_id,
                             @ModelAttribute OrderRequest orderRequest)
    {
        userService.createOrder(product_id, orderRequest);
        return "redirect:/";
    }


    //Search product by time and address
    @GetMapping("/{product_id}")
    public ResponseEntity<ResponseObject> getProductByID(@PathVariable("product_id") Long product_id) {
        Optional<Product> product = productRepository.findById(product_id);
        if (product.isPresent()) {
            return ResponseEntity.ok(new ResponseObject("200", "Product found", product.get()));
        }
        return ResponseEntity.ok(new  ResponseObject("404", "Product not found", null));
    }

}
