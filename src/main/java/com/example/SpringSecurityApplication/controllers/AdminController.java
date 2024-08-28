package com.example.SpringSecurityApplication.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdminController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String example() {
        return "Hello World";
    }
}
