package com.home.springSecurity.repository;

import com.home.springSecurity.dto.RegisterRequest;
import com.home.springSecurity.entity.Role;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UserArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(RegisterRequest.builder().firstName("jubayer").lastName("bin Hedayet")
                .email("jubu@gmail.com").password("123").role(Role.ADMIN).build()),
                Arguments.of(RegisterRequest.builder().firstName("jubayer").lastName("bin Hedayet")
                .email("jubu@gmail.com").password("").role(Role.ADMIN).build())
        );
    }
}
