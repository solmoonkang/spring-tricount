package kr.co.springtricount.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.infra.security.MemberDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberDetailService memberDetailService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable();

        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/api/v1/members/signup", "api/v1/fail").permitAll()
                .requestMatchers("/api/v1/**").authenticated();

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
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            ResponseFormat<Void> responseFormat = ResponseFormat.failureMessage(
                    ResponseStatus.FAIL_UNAUTHORIZED,
                    ResponseStatus.FAIL_UNAUTHORIZED.getMessage()
            );

            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(responseFormat));
        };
    }
}
