package com.prototype.springsecurity.controller;

import com.prototype.springsecurity.dtos.LoginResponseDto;
import com.prototype.springsecurity.dtos.LoginUserDto;
import com.prototype.springsecurity.dtos.RefreshTokenRequestDto;
import com.prototype.springsecurity.dtos.RegisterUserDto;
import com.prototype.springsecurity.model.User;
import com.prototype.springsecurity.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
        return ResponseEntity.ok(authenticationService.login(loginUserDto));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequestDto));
    }
}
