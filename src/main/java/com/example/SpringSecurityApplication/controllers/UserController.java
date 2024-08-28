package com.example.SpringSecurityApplication.controllers;

import com.example.SpringSecurityApplication.model.RefreshToken;
import com.example.SpringSecurityApplication.model.UserInfo;
import com.example.SpringSecurityApplication.model.dto.AuthRequestDTO;
import com.example.SpringSecurityApplication.model.dto.JwtResponseDTO;
import com.example.SpringSecurityApplication.model.dto.RefreshTokenRequestDTO;
import com.example.SpringSecurityApplication.service.JwtService;
import com.example.SpringSecurityApplication.service.RefreshTokenService;
import com.example.SpringSecurityApplication.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    private final UserInfoService service;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserInfoService service, JwtService jwtService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Регистрация нового пользователя", requestBody =
        @RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = UserInfo.class))))
    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody UserInfo userInfo) {
        String response = service.addUser(userInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Аутентификация и получение access и refresh токенов", requestBody =
        @RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = AuthRequestDTO.class))))
    @PostMapping("/login")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            return JwtResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                    .refreshToken(refreshToken.getToken())
                    .build();

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @Operation(summary = "Получчение нового refresh токена", requestBody =
        @RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = RefreshTokenRequestDTO.class))))
    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        JwtResponseDTO result =  refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenRequestDTO.getRefreshToken()).build();
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
        return result;
    }

    @Operation(summary = "Тестовый эндпоинт")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user")
    public String hello() {
        return "Hello World!";
    }
}
