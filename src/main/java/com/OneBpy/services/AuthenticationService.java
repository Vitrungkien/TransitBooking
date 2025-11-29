package com.OneBpy.services;

import com.OneBpy.dtos.JwtAuthenticationResponse;
import com.OneBpy.dtos.RefreshTokenRequest;
import com.OneBpy.dtos.SignUpRequest;
import com.OneBpy.dtos.SignInRequest;

public interface AuthenticationService {

    void signUp(SignUpRequest signUpRequest);
    void signUpSeller(SignUpRequest signUpRequest);
    JwtAuthenticationResponse logIn(SignInRequest signInRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
