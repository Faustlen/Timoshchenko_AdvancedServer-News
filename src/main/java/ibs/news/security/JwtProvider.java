package ibs.news.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public String generateToken(String mail) {

        return "Bearer " + Jwts.builder()
                .subject(mail)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + validityInMilliseconds))
                .signWith(getSignInKey())
                .compact();

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseSignedClaims(token);
        return true;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
