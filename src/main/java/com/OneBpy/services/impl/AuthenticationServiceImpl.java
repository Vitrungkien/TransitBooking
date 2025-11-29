package com.OneBpy.services.impl;

import com.OneBpy.dtos.JwtAuthenticationResponse;
import com.OneBpy.dtos.RefreshTokenRequest;
import com.OneBpy.dtos.SignUpRequest;
import com.OneBpy.dtos.SignInRequest;
import com.OneBpy.models.Role;
import com.OneBpy.models.Store;
import com.OneBpy.models.User;
import com.OneBpy.repositories.StoreRepository;
import com.OneBpy.repositories.UserRepository;
import com.OneBpy.services.AuthenticationService;
import com.OneBpy.services.JWTService;
import com.OneBpy.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final SellerService sellerService;

    public void signUp(SignUpRequest signUpRequest) {
        saveUser(signUpRequest, Role.ROLE_USER);
    }

    public void signUpSeller(SignUpRequest signUpRequest) {
        User newSeller = saveUser(signUpRequest, Role.ROLE_SELLER);
        Store newStore = sellerService.creatStore(newSeller);
        newStore.setUser(userRepository.save(newSeller));
        storeRepository.save(newStore);
    }

    private User saveUser(SignUpRequest signUpRequest, Role role) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setRole(role);
        return userRepository.save(user);
    }

    public JwtAuthenticationResponse logIn(SignInRequest signInRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                    signInRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            // Xử lý khi thông tin đăng nhập không hợp lệ (sai mật khẩu hoặc tài khoản)
            throw new BadCredentialsException("Invalid email or password.");
        }

        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setRole(user.getRole().name());
        return jwtAuthenticationResponse;
    }


    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }

}
