package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;



    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims= extractAllClaim(token);
        return claimsResolver.apply(claims);
    }


    public String generateToken(Map<String,Object> extractClaims, UserDetails details) {
        return builderToken(extractClaims,details,jwtExpiration);

    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public boolean isTokenValid(String token, UserDetails details){
        final String username = extractUsername(token);
        return (username.equals(details.getUsername())) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }


    private Claims extractAllClaim(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSiggnKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    public String builderToken(
            Map<String,Object> extraClaims,UserDetails userDetails, long expiration){
        return Jwts.
                builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()))
                .signWith(getSiggnKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private Key getSiggnKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserEntity authenticateUser) {
        return generateToken(new HashMap<>(),authenticateUser);
    }
}
