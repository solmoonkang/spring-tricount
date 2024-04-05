package kr.co.springtricount.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.annotation.Login;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.service.LoginService;
import kr.co.springtricount.service.dto.MemberResDTO;
import kr.co.springtricount.service.dto.request.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.springtricount.infra.config.SessionConstant.LOGIN_MEMBER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseFormat<MemberResDTO> login(HttpServletRequest request,
                                              @Validated @RequestBody LoginDTO login) {

        loginService.login(login.identity(), login.password());

        HttpSession httpSession = request.getSession();

        httpSession.getAttribute(LOGIN_MEMBER);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }

    @PostMapping("/logout")
    public ResponseFormat<Void> logout(HttpServletRequest request,
                                       @Login MemberResDTO member) {

        HttpSession httpSession = request.getSession(false);

        httpSession.invalidate();

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
