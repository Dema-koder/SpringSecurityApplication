package com.example.SpringSecurityApplication.controllers;

import com.example.SpringSecurityApplication.model.RefreshToken;
import com.example.SpringSecurityApplication.model.UserInfo;
import com.example.SpringSecurityApplication.model.dto.AuthRequestDTO;
import com.example.SpringSecurityApplication.model.dto.JwtResponseDTO;
import com.example.SpringSecurityApplication.model.dto.RefreshTokenRequestDTO;
import com.example.SpringSecurityApplication.service.JwtService;
import com.example.SpringSecurityApplication.service.RefreshTokenService;
import com.example.SpringSecurityApplication.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewUser() {
        UserInfo userInfo = new UserInfo();
        when(userInfoService.addUser(userInfo)).thenReturn("User created successfully");

        ResponseEntity<String> response = userController.addNewUser(userInfo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User created successfully", response.getBody());
        verify(userInfoService, times(1)).addUser(userInfo);
    }

    @Test
    public void testAuthenticateAndGetToken_Success() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO("testUser", "testPass");
        Authentication authentication = mock(Authentication.class);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refreshToken");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken("testUser")).thenReturn("accessToken");
        when(refreshTokenService.createRefreshToken("testUser")).thenReturn(refreshToken);

        JwtResponseDTO response = userController.authenticateAndGetToken(authRequestDTO);

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    public void testAuthenticateAndGetToken_Failure() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO("testUser", "testPass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("invalid user request..!!"));

        assertThrows(UsernameNotFoundException.class, () -> {
            userController.authenticateAndGetToken(authRequestDTO);
        });
    }

    @Test
    public void testRefreshToken_Success() {
        RefreshTokenRequestDTO refreshTokenRequestDTO = new RefreshTokenRequestDTO("validRefreshToken");

        RefreshToken refreshToken = mock(RefreshToken.class);
        UserInfo userInfo = mock(UserInfo.class);

        when(refreshTokenService.findByToken("validRefreshToken")).thenReturn(java.util.Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
        when(refreshToken.getUserInfo()).thenReturn(userInfo);
        when(userInfo.getUsername()).thenReturn("testUser");
        when(jwtService.generateToken("testUser")).thenReturn("newAccessToken");

        JwtResponseDTO response = userController.refreshToken(refreshTokenRequestDTO);

        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("validRefreshToken", response.getRefreshToken());
    }

    @Test
    public void testRefreshToken_Failure() {
        RefreshTokenRequestDTO refreshTokenRequestDTO = new RefreshTokenRequestDTO("invalidRefreshToken");

        when(refreshTokenService.findByToken("invalidRefreshToken")).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userController.refreshToken(refreshTokenRequestDTO);
        });
    }

    @Test
    public void testHello() {
        String response = userController.hello();
        assertEquals("Hello World!", response);
    }
}