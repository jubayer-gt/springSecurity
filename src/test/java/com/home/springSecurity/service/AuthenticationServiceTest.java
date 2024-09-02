package com.home.springSecurity.service;

import com.home.springSecurity.dto.RegisterRequest;
import com.home.springSecurity.repository.UserArgumentProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AuthenticationServiceTest {
    @Autowired
    AuthenticationService authenticationService;

    @ParameterizedTest
    @ArgumentsSource(UserArgumentProvider.class)
    public void testRegisterTest(RegisterRequest request){
        assertNotNull(authenticationService.register(request));
    }
}
