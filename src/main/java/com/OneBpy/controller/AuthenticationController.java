package com.OneBpy.controller;

import com.OneBpy.dtos.*;
import com.OneBpy.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public String signIn(@ModelAttribute("signInRequest") SignInRequest signInRequest,
                                                            HttpServletResponse response) {
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.logIn(signInRequest);
        String token = jwtAuthenticationResponse.getToken();
        // Thêm token vào cookie
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setMaxAge((int) TimeUnit.MINUTES.toSeconds(60)); // Thời gian sống của token
        cookie.setPath("/");

        String role = jwtAuthenticationResponse.getRole();
        Cookie cookieRole = new Cookie("Role", role);
        cookieRole.setMaxAge((int) TimeUnit.MINUTES.toSeconds(60)); // Thời gian sống của token
        cookieRole.setPath("/");

        response.addCookie(cookie);
        response.addCookie(cookieRole);
        response.addHeader("Role", role);
        response.addHeader("Authorization", "Bearer " + token );

        return "redirect:/";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signUpRequest") SignUpRequest signUpRequest) {
        authenticationService.signUp(signUpRequest);
        return "login";
    }

    @PostMapping("/signup-seller")
    public String signupSeller(@ModelAttribute("signUpRequest") SignUpRequest signUpRequest) {
        authenticationService.signUpSeller(signUpRequest);
        return "login";
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        CookieUtil.clearCookie(response, "Authorization");
        return ResponseEntity.ok("Logout successful");
    }

}
