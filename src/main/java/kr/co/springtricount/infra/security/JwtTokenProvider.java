package kr.co.springtricount.infra.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private static final long ACCESS_TOKEN_EXPIRY_TIME = 1000 * 60 * 30L;

	private static final long REFRESH_TOKEN_EXPIRY_TIME = 1000 * 60 * 60L * 24 * 7;

	private static final String AUTHORITY_CLAIMS = "auth";

	private static final String GRANT_TYPE = "Bearer";

	private static final String COMMA = ",";

	private static final String BLANK = "";

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
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

		// RefreshToken을 생성한다.
		Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRY_TIME);
		String refreshToken = Jwts.builder()
			.setExpiration(refreshTokenExpiresIn)
			.signWith(key, SignatureAlgorithm.HS256)
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
			.stream(claims.get(AUTHORITY_CLAIMS).toString().split(COMMA))
			.map(SimpleGrantedAuthority::new)
			.toList();

		// UserDetails 객체를 만들어서 Authentication을 반환한다.
		// UserDetails은 인터페이스이며, User는 UserDetails를 구현한 클래스이다.
		UserDetails principal = new User(claims.getSubject(), BLANK, authorities);

		return new UsernamePasswordAuthenticationToken(principal, BLANK, authorities);
	}

	// 토큰 정보를 검증하는 메서드이다.
	public boolean validateToken(String token) {

		Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);

		return true;
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
