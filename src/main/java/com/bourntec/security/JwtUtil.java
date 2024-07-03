package com.bourntec.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
	@Value("${spring.security.jwt.secret_key}")
    private String secret_key="Vg5uX4bEBvMCHxtLpU4GmJFqRojZl4qH9l1kYe9e2Vg";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret_key.getBytes())).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
//    	System.out.println("generate");
        Map<String, Object> claims = new HashMap<>();
        String str=createToken(claims, userDetails.getUsername());
//        System.out.println(str);
 
//        return createToken(claims, userDetails.getUsername());
        return str;
    }

    private String createToken(Map<String, Object> claims, String subject) {
//    	System.out.println("create");
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                .signWith(Keys.hmacShaKeyFor(secret_key.getBytes())).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
//        System.out.print("Hello"+token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
