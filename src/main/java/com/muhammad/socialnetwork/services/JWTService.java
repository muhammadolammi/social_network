package com.muhammad.socialnetwork.services;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;


@Service
public class JWTService {
    // create a  final signer 
    private final SecretKey JWTSIGNER;
    
    public JWTService() {
        // initialize the signer with a value
        KeyGenerator keyGenerator = null;
        try {
             keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JWTSIGNER = keyGenerator.generateKey();

    }





    public String generateToken(String subject){
        Map<String, Object> claims = new HashMap<>();
        
        return Jwts
        .builder()
        .claims()
        .add(claims)
        .subject(subject)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
        .and()
        .signWith(JWTSIGNER)
        .compact();
    }




  private SecretKey getKey() {
        return JWTSIGNER;
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
