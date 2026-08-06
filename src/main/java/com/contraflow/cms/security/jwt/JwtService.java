package com.contraflow.cms.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtBuilder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

    }

    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();

    }

    // Token variant that records WHICH user type it belongs to (ADMIN / TENANT).
    // The filter uses this "type" claim to load the user from the correct table.
    public String generateToken(UserDetails userDetails, String userType) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("type", userType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();

    }

    // Token that embeds arbitrary user details (userId, firstName, role, tenantId, ...)
    // as claims, so the backend can read the current user straight from the token.
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {

        JwtBuilder builder = Jwts.builder()
                .subject(userDetails.getUsername());        // sub = email

        if (extraClaims != null) {
            extraClaims.forEach(builder::claim);
        }

        return builder
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUserType(String token) {

        return extractClaim(token, claims -> claims.get("type", String.class));

    }

    // ----- convenience extractors so callers can parse user details from the token -----

    public Long extractUserId(String token) {
        Object v = extractClaim(token, claims -> claims.get("userId"));
        return (v instanceof Number n) ? n.longValue() : null;
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Long extractTenantId(String token) {
        Object v = extractClaim(token, claims -> claims.get("tenantId"));
        return (v instanceof Number n) ? n.longValue() : null;
    }

    public String extractFirstName(String token) {
        return extractClaim(token, claims -> claims.get("firstName", String.class));
    }


    private Claims extractAllClaims(String token) {

        return Jwts
                .parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);

    }

    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );

    }
    public Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );

    }
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());

    }


}
