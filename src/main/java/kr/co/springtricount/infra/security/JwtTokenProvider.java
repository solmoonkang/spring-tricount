package kr.co.springtricount.infra.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.springtricount.infra.exception.UnauthorizedAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final long ACCESS_TOKEN_EXPIRY_TIME = 86400000;

    private static final long REFRESH_TOKEN_EXPIRY_TIME = 2592000000L;

    private static final String AUTHORITY_CLAIMS = "auth";

    private static final String GRANT_TYPE = "Bearer";

    private static final String COMMA = ",";

    private final Key key;

    // application.yml에서 설정한 secret값을 가져와서 Key에 저장한다.
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Member 정보를 통해 AccessToken & RefreshToken을 생성하는 메서드이다.
    public JwtToken generateToken(Authentication authentication) {

        // 권한을 가져온다.
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(COMMA));

        long now = (new Date()).getTime();

        // AccessToken을 생성한다.
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRY_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITY_CLAIMS, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.RS256)
                .compact();

        // RefreshToken을 생성한다.
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRY_TIME);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.RS256)
                .compact();

        return JwtToken.builder()
                .grantType(GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드이다.
    public Authentication getAuthentication(String accessToken) {

        // JWT 토큰을 복호화한다.
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITY_CLAIMS) == null) {
            throw new RuntimeException("[❎ERROR] 권한 정보가 없는 토큰입니다.");
        }

        // Claims에서 권한 정보를 가져온다.
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        // UserDetails 객체를 만들어서 Authentication을 반환한다.
        // UserDetails은 인터페이스이며, User는 UserDetails를 구현한 클래스이다.
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드이다.
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new UnauthorizedAccessException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new UnauthorizedAccessException("JWT 토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new UnauthorizedAccessException("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new UnauthorizedAccessException("JWT 토큰이 잘못되었습니다.");
        }
    }

    // AccessToken
    private Claims parseClaims(String accessToken) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
