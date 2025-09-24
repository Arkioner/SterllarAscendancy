package com.arkioner.infrastructure.security;

import com.arkioner.domain.loginuser.LoginUser;
import com.arkioner.domain.loginuser.LoginUserId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    private final Key key;
    private final long expirationMs;

    public JwtUtils(org.springframework.core.env.Environment env) {
        String secret = env.getProperty("jwt.secret", "change-me-to-a-strong-secret");
        this.expirationMs = Long.parseLong(env.getProperty("jwt.expiration-ms", "86400000"));
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(LoginUser loginUser) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setId(loginUser.id().value().toString())
                .setSubject(loginUser.username())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

