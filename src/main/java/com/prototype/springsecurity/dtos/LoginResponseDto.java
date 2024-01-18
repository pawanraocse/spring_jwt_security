package com.prototype.springsecurity.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class LoginResponseDto {
    private String token;
    private String refreshToken;
    private long expiresIn;
}
