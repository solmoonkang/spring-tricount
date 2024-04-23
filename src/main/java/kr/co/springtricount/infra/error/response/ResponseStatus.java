package kr.co.springtricount.infra.error.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseStatus {

	// SUCCESS
	SUCCESS_EXECUTE(HttpStatus.OK, "✅ [SUCCESS] 요청이 성공적으로 처리되었습니다."),
	SUCCESS_CREATED(HttpStatus.CREATED, "✅ [SUCCESS] 리소스가 성공적으로 생성되었습니다."),

	// FAIL
	FAIL_BAD_REQUEST(HttpStatus.BAD_REQUEST, "❎ [ERROR] 잘못된 요청입니다. 요청 형식을 확인해주세요."),
	FAIL_DUPLICATED(HttpStatus.CONFLICT, "❎ [ERROR] 중복된 요청입니다. 다른 요청 형식을 입력해주세요."),
	FAIL_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "❎ [ERROR] 인증되지 않은 사용자 입니다."),
	FAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "❎ [ERROR] 요청한 리소스를 찾을 수 없습니다."),
	FAIL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "❎ [ERROR] 서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),

	// AUTHENTICATION
	FAIL_UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "❎ [ERROR] 이 기능을 사용하기 위해서는 로그인이 필요합니다."),
	FAIL_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "❎ [ERROR] 로그인 정보가 올바르지 않습니다. 아이디 또는 비밀번호를 확인해주세요."),
	FAIL_INACTIVE_ACCOUNT(HttpStatus.UNAUTHORIZED, "❎ [ERROR] 비활성화된 계정입니다. 관리자에게 문의해주세요."),
	FAIL_ACCOUNT_LOCKED(HttpStatus.UNAUTHORIZED, "❎ [ERROR] 계정이 잠겼습니다. 잠금 해제를 위해 관리자에게 문의해주세요."),
	FAIL_CREDENTIALS_EXPIRED(HttpStatus.UNAUTHORIZED, "❎ [ERROR] 비밀번호가 만료되었습니다. 비밀번호를 변경해주세요."),

	// MEMBER
	FAIL_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "❎ [ERROR] 요청한 회원을 찾을 수 없습니다."),
	FAIL_IDENTITY_DUPLICATION(HttpStatus.CONFLICT, "❎ [ERROR] 해당 아이디는 이미 사용 중입니다."),
	FAIL_IDENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "❎ [ERROR] 요청한 회원 아이디를 찾을 수 없습니다."),

	// SETTLEMENT
	FAIL_SETTLEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "❎ [ERROR] 요청한 정산을 찾을 수 없습니다."),

	// EXPENSE
	FAIL_EXPENSE_NOT_FOUND(HttpStatus.NOT_FOUND, "❎ [ERROR] 요청한 지출 내역을 찾을 수 없습니다."),

	// CHAT ROOM
	FAIL_CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "❎ [ERROR] 요청한 채팅방을 찾을 수 없습니다.");

	private HttpStatus status;

	private String message;
}
