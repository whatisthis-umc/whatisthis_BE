package umc.demoday.whatisthis.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKeyString;
    @Value("${JWT_LINK_SECRET}")
    private String linkSecretString;

    private Key linkKey;
    private Key secretKey;

    private final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30;     // 30분
    private final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        this.linkKey   = Keys.hmacShaKeyFor(linkSecretString.getBytes());
    }

    // AccessToken 생성
    public String createAccessToken(Integer memberId, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        claims.put("memberId", memberId);
        claims.put("role", role);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getJti(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().getId();
    }

    public Date getExpiration(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().getExpiration();
    }

    // RefreshToken 생성 (payload 최소화, DB에서 추적)
    public String createRefreshToken(Integer memberId) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 사용자 ID 추출
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }

    // 토큰에서 Role 추출
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public String createLinkToken(String email, String provider, String providerId, Duration ttl) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claim("typ", "link")
                .claim("email", email)
                .claim("provider", provider)
                .claim("providerId", providerId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ttl)))
                .signWith(linkKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public LinkClaims verifyLinkToken(String token) {
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(linkKey).build().parseClaimsJws(token);
        Claims c = jws.getBody();
        if (!"link".equals(c.get("typ"))) throw new IllegalArgumentException("Invalid token type");
        return new LinkClaims(
                c.get("email", String.class),
                c.get("provider", String.class),
                c.get("providerId", String.class)
        );
    }

    public record LinkClaims(String email, String provider, String providerId) {}

}

