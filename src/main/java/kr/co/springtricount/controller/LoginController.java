package kr.co.springtricount.controller;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    @GetMapping()
    public ResponseFormat<Void> mainPage() {

        return ResponseFormat.successMessage(
                ResponseStatus.SUCCESS_EXECUTE,
                "main-page"
        );
    }

    @GetMapping("/fail")
    public ResponseFormat<Void> fail() {

        return ResponseFormat.failureMessage(
                ResponseStatus.FAIL_BAD_REQUEST,
                "login-fail"
        );
    }
}
