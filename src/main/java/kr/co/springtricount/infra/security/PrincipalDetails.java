package kr.co.springtricount.infra.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrincipalDetails implements OAuth2User, UserDetails {

	private final String username;

	private final String password;

	private final Collection<? extends GrantedAuthority> authorities;

	private final Map<String, Object> oAuth2Attributes;

	// SpringSecurity 필수 메서드 재정의
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	// OAuth2 Client 필수 메서드 재정의
	@Override
	public Map<String, Object> getAttributes() {
		return oAuth2Attributes;
	}

	@Override
	public String getName() {
		return username;
	}
}
