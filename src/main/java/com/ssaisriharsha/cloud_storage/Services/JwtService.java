package com.ssaisriharsha.cloud_storage.Services;

import com.ssaisriharsha.cloud_storage.Repos.UserRepo;
import com.ssaisriharsha.cloud_storage.SecurityConfig.SecureUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final long EXPIRATION=30*24*60*60*100;
    private final String SECRET;
    private final UserRepo repo;
    public JwtService(@Value("${security.jwt.secret}") String secret, UserRepo repo) {
        this.repo=repo;
        this.SECRET=secret;
    }
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }
    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        Claims claims=extractClaims(token);
        return claims.getSubject();
    }

    private boolean usedBeforeIssuing(String token) {
        Claims claims=extractClaims(token);
        return claims.getIssuedAt().before(new Date());
    }

    private boolean usedAfterExpiration(String token) {
        Claims claims=extractClaims(token);
        return claims.getExpiration().after(new Date());
    }

    private boolean userExists(String token) {
        return repo.existsByEmail(extractEmail(token));
    }

    public boolean isJwtValid(String token) {
        return userExists(token)&&!usedBeforeIssuing(token)&&!usedAfterExpiration(token);
    }


    public String generateJwt(SecureUser user) {
        return Jwts
                .builder()
                .signWith(getSigningKey())
                .header()
                .add("typ", "JWT")
                .and()
                .subject(user.getEmail())
                .claim("user", user.getUsername())
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION))
                .issuedAt(new Date())
                .compact();
    }
}
