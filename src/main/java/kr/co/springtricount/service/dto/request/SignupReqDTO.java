package kr.co.springtricount.service.dto.request;

import jakarta.validation.constraints.NotNull;

public record SignupReqDTO(
	@NotNull(message = "아이디를 입력해주세요.") String identity,
	@NotNull(message = "이름을 입력해주세요.") String name,
	@NotNull(message = "비밀번호를 입력해주세요.") String password
) {
}
