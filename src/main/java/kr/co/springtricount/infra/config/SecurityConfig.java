package kr.co.springtricount.infra.config;

import kr.co.springtricount.infra.security.MemberDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberDetailService memberDetailService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable();

        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/members/signup").permitAll()
                .requestMatchers("/api/v1/**").authenticated();

        httpSecurity
                .formLogin().permitAll()
                .usernameParameter("identity")
                .defaultSuccessUrl("/api/v1")
                .failureUrl("/api/v1/fail");

        httpSecurity
                .logout()
                .logoutSuccessUrl("/login");

        httpSecurity.userDetailsService(memberDetailService);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
