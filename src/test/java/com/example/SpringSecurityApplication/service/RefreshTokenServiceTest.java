package com.example.SpringSecurityApplication.service;

import com.example.SpringSecurityApplication.model.RefreshToken;
import com.example.SpringSecurityApplication.model.UserInfo;
import com.example.SpringSecurityApplication.repository.RefreshTokenRepository;
import com.example.SpringSecurityApplication.repository.UserInfoRepository;
import com.example.SpringSecurityApplication.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserInfoRepository userRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        Field refreshTokenExpirationField = RefreshTokenService.class.getDeclaredField("refreshTokenExpiration");
        refreshTokenExpirationField.setAccessible(true);
        refreshTokenExpirationField.set(refreshTokenService, Duration.ofMillis(60000)); // 1 минута
    }

    @Test
    void testCreateRefreshToken_Success() {
        UserInfo user = new UserInfo();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken("testUser");

        assertNotNull(refreshToken);
        assertEquals(user, refreshToken.getUserInfo());
        assertNotNull(refreshToken.getToken());
        assertTrue(refreshToken.getExpiryDate().isAfter(Instant.now()));

        verify(userRepository, times(1)).findByUsername("testUser");
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void testFindByToken_Success() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> foundToken = refreshTokenService.findByToken(token);

        assertTrue(foundToken.isPresent());
        assertEquals(token, foundToken.get().getToken());

        verify(refreshTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void testVerifyExpiration_TokenNotExpired() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plus(Duration.ofMinutes(1)));

        RefreshToken verifiedToken = refreshTokenService.verifyExpiration(refreshToken);

        assertEquals(refreshToken, verifiedToken);
    }

    @Test
    void testVerifyExpiration_TokenExpired() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().minus(Duration.ofMinutes(1)));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> refreshTokenService.verifyExpiration(refreshToken));
        assertEquals(refreshToken.getToken() + " Refresh token is expired. Please make a new login..!", exception.getMessage());

        verify(refreshTokenRepository, times(1)).delete(refreshToken);
    }
}