package com.example.SpringSecurityApplication.service;

import com.example.SpringSecurityApplication.model.UserInfo;
import com.example.SpringSecurityApplication.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInfoServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserInfoService userInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        String username = "testUser";
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword("password");
        userInfo.setRoles("USER,ADMIN");

        when(userInfoRepository.findByUsername(username)).thenReturn(Optional.of(userInfo));

        UserDetails userDetails = userInfoService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        verify(userInfoRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "nonExistentUser";
        when(userInfoRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userInfoService.loadUserByUsername(username));

        assertEquals("User not found " + username, exception.getMessage());
        verify(userInfoRepository, times(1)).findByUsername(username);
    }

    @Test
    void testAddUser_Success() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("newUser");
        userInfo.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        String result = userInfoService.addUser(userInfo);

        assertEquals("User Added Successfully", result);
        assertEquals("encodedPassword", userInfo.getPassword());
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(userInfoRepository, times(1)).save(userInfo);
    }
}