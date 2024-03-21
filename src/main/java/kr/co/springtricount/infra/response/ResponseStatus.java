package kr.co.springtricount.infra.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseStatus {

    SUCCESS_EXECUTE(HttpStatus.OK, "✅ [SUCCESS] 요청이 성공적으로 처리되었습니다."),
    SUCCESS_CREATED(HttpStatus.CREATED, "✅ [SUCCESS] 리소스가 성공적으로 생성되었습니다."),


    FAIL_BAD_REQUEST(HttpStatus.BAD_REQUEST, "❎ [ERROR] 잘못된 요청입니다. 요청 형식을 확인해주세요."),
    FAIL_DUPLICATED(HttpStatus.CONFLICT, "❎ [ERROR] 중복된 요청입니다. 다른 요청 형식을 입력해주세요."),
    FAIL_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "❎ [ERROR] 인증되지 않은 사용자 입니다."),
    FAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "❎ [ERROR] 요청한 리소스를 찾을 수 없습니다."),
    FAIL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "❎ [ERROR] 서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),


    FAIL_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "❎ [ERROR] 요청한 회원을 찾을 수 없습니다."),
    FAIL_IDENTITY_DUPLICATION(HttpStatus.CONFLICT, "❎ [ERROR] 해당 아이디는 이미 사용 중입니다.");

    private HttpStatus status;

    private String message;
}
