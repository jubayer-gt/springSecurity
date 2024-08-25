package com.home.springSecurity.controller;

import com.home.springSecurity.dto.AuthenticationRequest;
import com.home.springSecurity.dto.AuthenticationResponse;
import com.home.springSecurity.dto.RegisterRequest;
import com.home.springSecurity.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class DemoController {

    @GetMapping()
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("I am jubayer with Jwt Token");
    }

}
