package com.home.springSecurity.controller;

import com.home.springSecurity.dto.AuthenticationRequest;
import com.home.springSecurity.dto.AuthenticationResponse;
import com.home.springSecurity.dto.RegisterRequest;
import com.home.springSecurity.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> Login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
