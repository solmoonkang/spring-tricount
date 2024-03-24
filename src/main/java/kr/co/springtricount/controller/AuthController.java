package kr.co.springtricount.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.AuthService;
import kr.co.springtricount.service.dto.request.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseFormat<Void> login(HttpSession httpSession,
                                      @RequestBody @Validated LoginDTO loginDTO) {

        authService.login(httpSession, loginDTO);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }

    @PostMapping("/logout")
    public ResponseFormat<Void> logout(HttpSession httpSession) {

        authService.logout(httpSession);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
