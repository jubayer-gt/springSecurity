package com.home.springSecurity.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TokenRepositoryTest {
    @Autowired
    private TokenRepository tokenRepository;

    @Disabled
    @Test
    public void testFindByAccessToken(){
        assertEquals(5,3+2);
        assertTrue(tokenRepository.findByAccessToken("ey.jHsz8PiRICDVI89mB51S54Jcj").isPresent());
        assertTrue(3>5);
    }

    @ParameterizedTest
    @CsvSource({
            "1,2,3",
            "2,4,6",
            "5,4,8"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected,a+b);
    }
}
