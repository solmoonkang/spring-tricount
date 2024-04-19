package kr.co.springtricount.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberDetailService memberDetailService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(
                authorizeHttpRequest -> authorizeHttpRequest
                        .requestMatchers("/api/v1/members/signup").permitAll()
                        .requestMatchers("/api/v1/fail").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                );

        httpSecurity.formLogin().permitAll()
                .usernameParameter("identity")
                .defaultSuccessUrl("/api/v1")
                .failureUrl("/api/v1/fail");

        httpSecurity.logout()
                .logoutSuccessUrl("/login");

        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint());

        httpSecurity.userDetailsService(memberDetailService);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
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
