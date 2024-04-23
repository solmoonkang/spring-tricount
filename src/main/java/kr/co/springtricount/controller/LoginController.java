package kr.co.springtricount.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.springtricount.infra.error.response.ResponseFormat;
import kr.co.springtricount.infra.error.response.ResponseStatus;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

	@GetMapping()
	public ResponseFormat<Void> mainPage() {

		return ResponseFormat.successMessage(
			ResponseStatus.SUCCESS_EXECUTE,
			ResponseStatus.SUCCESS_EXECUTE.getMessage()
		);
	}

	@GetMapping("/fail")
	public ResponseFormat<Void> fail() {

		return ResponseFormat.failureMessage(
			ResponseStatus.FAIL_BAD_REQUEST,
			ResponseStatus.FAIL_BAD_REQUEST.getMessage()
		);
	}
}
