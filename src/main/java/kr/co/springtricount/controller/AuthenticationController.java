package kr.co.springtricount.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.AuthenticationService;
import kr.co.springtricount.service.dto.request.LoginReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseFormat<Void> login(HttpSession httpSession,
                                      @RequestBody @Validated LoginReqDTO loginReqDTO) {

        authenticationService.login(httpSession, loginReqDTO);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }

    @PostMapping("/logout")
    public ResponseFormat<Void> logout(HttpSession httpSession) {

        authenticationService.logout(httpSession);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
