package kr.co.springtricount.infra.security;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final MemberDetailService memberDetailService;

	private final JwtTokenProvider jwtTokenProvider;

	private final OAuth2MemberService oAuth2MemberService;

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		// REST API 설정
		httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.headers(header -> header.frameOptions(
				HeadersConfigurer.FrameOptionsConfig::disable).disable())
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Request 인증 및 인가 설정
		httpSecurity.authorizeHttpRequests(
			authorizeHttpRequest -> authorizeHttpRequest
				.requestMatchers("/api/v1/members/signup").permitAll()
				.requestMatchers("/api/v1/fail").permitAll()
				.requestMatchers("/api/v1/**").authenticated()
		);

		// OAuth2 설정
		httpSecurity.oauth2Login(oauth2Configurer -> oauth2Configurer
			.loginPage("/api/v1/auth/login")
			.successHandler(authenticationSuccessHandler())
			.userInfoEndpoint()
			.userService(oAuth2MemberService));

		// JWT 인증을 위해 직접 커스텀한 필터를 UsernamePasswordAuthenticationFilter 전에 실행한다.
		httpSecurity.addFilterBefore(
			new JwtAuthenticationFilter(jwtTokenProvider),
			UsernamePasswordAuthenticationFilter.class);

		// 인증 예외 핸들링
		httpSecurity.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint());

		httpSecurity.userDetailsService(memberDetailService);

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {

		return (request, response, authentication) -> {
			DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User)authentication.getPrincipal();

			String id = defaultOAuth2User.getAttributes().get("identity").toString();
			String body = """
				{"id":"%s"}
				""".formatted(id);

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());

			PrintWriter printWriter = response.getWriter();
			printWriter.println(body);
			printWriter.flush();
		};
	}

	private AuthenticationEntryPoint authenticationEntryPoint() {

		return (request, response, authException) -> {
			ResponseFormat<Void> responseFormat = ResponseFormat.failureMessage(
				ResponseStatus.FAIL_UNAUTHORIZED,
				ResponseStatus.FAIL_UNAUTHORIZED_ACCESS.getMessage()
			);

			ResponseEntity<ResponseFormat<Void>> responseEntity =
				new ResponseEntity<>(responseFormat, HttpStatus.UNAUTHORIZED);

			response.setStatus(responseEntity.getStatusCodeValue());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			PrintWriter printWriter = response.getWriter();
			printWriter.write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
			printWriter.flush();
		};
	}
}
