package com.prototype.springsecurity.service;

import com.prototype.springsecurity.dtos.LoginResponseDto;
import com.prototype.springsecurity.dtos.LoginUserDto;
import com.prototype.springsecurity.dtos.RefreshTokenRequestDto;
import com.prototype.springsecurity.dtos.RegisterUserDto;
import com.prototype.springsecurity.model.User;

public interface AuthenticationService {
    User signup(RegisterUserDto input);
    User authenticate(LoginUserDto input);
    String getRefreshToken(String token);

    LoginResponseDto login(LoginUserDto loginUserDto);
    LoginResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
}
