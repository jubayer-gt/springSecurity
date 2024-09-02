package com.home.springSecurity.repository;


import com.home.springSecurity.dto.RegisterRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "Doe.doe@example.com",
            "John.doe@example.com",
            "Jon.doe@example.com"
    })
    public void test(String email){
        assertTrue(userRepository.findByEmail(email).isPresent());
    }

    @ParameterizedTest
    @ValueSource( strings = {
            "Doe.doe@example.com",
            "John.doe@example.com",
            "Jon.doe@example.com"
    })
    public void testUsingValueSorce(String email){
        assertTrue(userRepository.findByEmail(email).isPresent());
    }



}
