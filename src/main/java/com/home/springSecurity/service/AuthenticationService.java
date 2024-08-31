package com.home.springSecurity.service;

import com.home.springSecurity.dto.AuthenticationRequest;
import com.home.springSecurity.dto.AuthenticationResponse;
import com.home.springSecurity.dto.RegisterRequest;
import com.home.springSecurity.entity.Token;
import com.home.springSecurity.entity.TokenType;
import com.home.springSecurity.entity.User;
import com.home.springSecurity.jwt.JwtService;
import com.home.springSecurity.repository.TokenRepository;
import com.home.springSecurity.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user= User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser=userRepository.save(user);
        var accessToken=jwtService.generateAccessToken(user);
        var refreshToken=jwtService.generateRefreshToken(user);
        revokeAllTokenByUser(user);
        savedUserToken(accessToken, refreshToken, savedUser);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
              request.getEmail(),
              request.getPassword()
        ));
        var user=userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var accessToken=jwtService.generateAccessToken(user);
        var refreshToken=jwtService.generateRefreshToken(user);
        revokeAllTokenByUser(user);
        savedUserToken(accessToken, refreshToken, user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        // extract the token from authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String refreshToken = authHeader.substring(7);
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new RuntimeException("No user found"));

        if(jwtService.isValidRefreshToken(refreshToken)) {
            String accessToken = jwtService.generateAccessToken(user);
            revokeAllTokenByUser(user);
            savedUserToken(accessToken, refreshToken, user);
            return new ResponseEntity(new AuthenticationResponse(accessToken, refreshToken), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    private void savedUserToken(String accessToken, String refreshToken, User user){
        var token= Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.Bearer)
                .loggedOut(false)
                .user(user)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllTokenByUser(User user){
        List<Token> validToken=tokenRepository.findAllAccessTokensByUser(user.getId());
        if (validToken.isEmpty())
            return;
        validToken.forEach(t->{
            t.setLoggedOut(true);
        });
        tokenRepository.saveAll(validToken);
    }
}
