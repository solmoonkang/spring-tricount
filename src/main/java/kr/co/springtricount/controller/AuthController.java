package kr.co.springtricount.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.springtricount.infra.error.response.ResponseFormat;
import kr.co.springtricount.infra.error.response.ResponseStatus;
import kr.co.springtricount.infra.security.JwtToken;
import kr.co.springtricount.infra.security.JwtTokenProvider;
import kr.co.springtricount.service.dto.request.LoginReqDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider jwtTokenProvider;

	@PostMapping("/login")
	public ResponseFormat<JwtToken> login(@RequestBody @Validated LoginReqDTO loginReqDTO) {

		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginReqDTO.identity(), loginReqDTO.password())
		);

		return ResponseFormat.successMessageWithData(
			ResponseStatus.SUCCESS_EXECUTE,
			jwtTokenProvider.generateToken(authentication)
		);
	}
}
