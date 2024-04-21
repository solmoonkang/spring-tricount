package kr.co.springtricount.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberDetailService memberDetailService;

    private final JwtTokenProvider jwtTokenProvider;

    private final OAuth2UserService oAuth2UserService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(
                authorizeHttpRequest -> authorizeHttpRequest
                        .requestMatchers("/api/v1/members/signup").permitAll()
                        .requestMatchers("/api/v1/fail").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                );

        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint());

        httpSecurity.userDetailsService(memberDetailService);

        // JWT 인증을 위해 직접 커스텀한 필터를 UsernamePasswordAuthenticationFilter 전에 실행한다.
        httpSecurity.addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class
        );

        httpSecurity.oauth2Login(oauth2Configurer -> oauth2Configurer
                .loginPage("/api/v1/auth/login")
                .successHandler(authenticationSuccessHandler())
                .userInfoEndpoint()
                .userService(oAuth2UserService)
        );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {

        return (request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

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

        return  (request, response, authException) -> {
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
