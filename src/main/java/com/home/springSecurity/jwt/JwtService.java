package com.home.springSecurity.jwt;

import com.home.springSecurity.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpire;
    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    @Autowired
    private  TokenRepository tokenRepository;
    public String generateAccessToken(UserDetails userDetails){
        return generateAccessToken(new HashMap<>(),userDetails);
    }
    public String generateAccessToken(
            Map<String, Object> extraClaims, UserDetails userDetails
    ){
        return buildToken(extraClaims,userDetails,accessTokenExpire);
    }

    public String generateRefreshToken( UserDetails userDetails
    ){
        return buildToken(new HashMap<>(),userDetails,refreshTokenExpire);
    }
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration){
        return Jwts
                .builder().claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration ))
                .signWith(getSignInKey())
                .compact();
    }

    public Boolean isTokenValid(String token){
        boolean validToken = tokenRepository
                .findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);
        return  !isTokenExpired(token) && validToken;
    }

    public Boolean isValidRefreshToken(String token){
        boolean validToken = tokenRepository
                .findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);
        return  !isTokenExpired(token) && validToken;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String jwt) {

        return extractClaim(jwt,Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
