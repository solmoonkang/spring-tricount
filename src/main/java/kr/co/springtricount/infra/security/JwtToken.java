package kr.co.springtricount.infra.security;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class JwtToken {

    private String grantType;

    private String accessToken;

    private String refreshToken;
}
