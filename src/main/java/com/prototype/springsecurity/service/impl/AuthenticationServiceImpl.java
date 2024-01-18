package com.prototype.springsecurity.service.impl;

import com.prototype.springsecurity.dtos.LoginResponseDto;
import com.prototype.springsecurity.dtos.LoginUserDto;
import com.prototype.springsecurity.dtos.RefreshTokenRequestDto;
import com.prototype.springsecurity.dtos.RegisterUserDto;
import com.prototype.springsecurity.exception.AdminUserAlreadyExistsException;
import com.prototype.springsecurity.model.Role;
import com.prototype.springsecurity.model.User;
import com.prototype.springsecurity.repository.UserRepository;
import com.prototype.springsecurity.service.AuthenticationService;
import com.prototype.springsecurity.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    @Override
    public User signup(final RegisterUserDto input) {
        if ("admin".equalsIgnoreCase(input.getRole())){
            final User adminUser = userRepository.findByRole(Role.ADMIN);
            if (adminUser != null) {
                throw new AdminUserAlreadyExistsException("Admin user already exists.");
            }
        }
        final Optional<User> email = userRepository.findByEmail(input.getEmail());
        if (email.isPresent()) {
            throw new RuntimeException("Email id already used");
        }
        User user = new User()
            .setFirstName(input.getFirstName())
            .setLastName(input.getLastName())
            .setEmail(input.getEmail())
            .setPassword(passwordEncoder.encode(input.getPassword()))
            .setRole(Role.valueOf(input.getRole().toUpperCase()));

        return userRepository.save(user);
    }

    @Override
    public User authenticate(final LoginUserDto input) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
            )
        );

        return userRepository.findByEmail(input.getEmail())
            .orElseThrow();
    }

    @Override
    public String getRefreshToken(String token) {
        final String email = jwtService.extractUsername(token);
        final User user = userRepository.findByEmail(email).orElseThrow();
        if (jwtService.isTokenValid(token, user)) {
            return jwtService.generateToken(user);
        }
        return null;
    }

    @Override
    public LoginResponseDto login(final LoginUserDto loginUserDto) {
        User authenticatedUser = authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        return new LoginResponseDto().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
    }

    @Override
    public LoginResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        final String refreshToken = getRefreshToken(refreshTokenRequestDto.getToken());
        return new LoginResponseDto().setToken(refreshTokenRequestDto.getToken())
            .setRefreshToken(refreshToken)
            .setExpiresIn(jwtService.getExpirationTime());
    }
}
