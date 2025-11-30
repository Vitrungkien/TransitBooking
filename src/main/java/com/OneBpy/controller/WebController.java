package com.OneBpy.controller;

import com.OneBpy.dtos.PDTO;
import com.OneBpy.dtos.SearchByKeywordRq;
import com.OneBpy.dtos.SearchForm;
import com.OneBpy.models.Product;
import com.OneBpy.repositories.ProductRepository;
import com.OneBpy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class WebController {
    private final ProductRepository productRepository;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    @GetMapping("/")
    public String Home(Model model) {
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("searchByKeywordRq", new SearchByKeywordRq());
        return "main";
    }
    @GetMapping("/search-by-stop")
    public String search(@ModelAttribute("searchForm") SearchForm searchForm,
                         @ModelAttribute("searchByKeywordRq") SearchByKeywordRq searchByKeywordRq,
                         Model model) {
        if (searchForm.getEndAddress() != null && searchForm.getStartAddress() != null &&
                searchForm.getStartTime1() != null) {
            LocalTime startTime2 = searchForm.getStartTime1().plusHours(1);
            List<Product> productList = productRepository.findProductsByTimeAndAddress(
                    searchForm.getStartTime1(), startTime2,
                    searchForm.getStartAddress(), searchForm.getEndAddress()
            );
            List<PDTO> products = userService.getAllProduct(productList);
            model.addAttribute("products", products);
        } else if (searchByKeywordRq.getKeyword() != null) {
            List<Product> productList = productRepository.findByKeyword(searchByKeywordRq.getKeyword());
            List<PDTO> products = userService.getAllProduct(productList);
            model.addAttribute("products", products);
        }
        return "search";
    }


    @GetMapping("/order")
    public String Order() {
        return "order";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/signup")
    public String showFormSignup() {
        return "signup";
    }
    @GetMapping("/profile")
    public String showProfile() {
        return "profile";
    }

    @GetMapping("/signup-seller")
    public String showFormSignupSeller() {
        return "signup-seller";
    }

    @GetMapping("/management")
    public String management() {
        return "management";
    }

    @GetMapping("/management-order")
    public String managementOrder() {
        return "management-order";
    }

    @GetMapping("/management-notice")
    public String managementNotice() {
        return "management-notice";
    }

    @GetMapping("/management/add-product")
    public String addProduct() {
        return "add-product";
    }


    //Trang cập nhật sản phẩm
    @GetMapping("/management/{product_id}/update-product")
    public String updateProduct(@PathVariable("product_id") Long productId) {
        // Thực hiện các xử lý cần thiết dựa trên productId
        return "update-product";
    }

    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("searchByKeywordRq", new SearchByKeywordRq());
        return "search";
    }

    @GetMapping("/my-order")
    public String myOrder() {
        return "my-order";
    }

    @GetMapping("/example/example")
    public String example() {
        return "pages/example";
    }

    @GetMapping("/example/main")
    public String homePage() {
        return "layout/main";
    }
}
