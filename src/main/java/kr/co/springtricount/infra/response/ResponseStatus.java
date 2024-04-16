package kr.co.springtricount.infra.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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
    FAIL_UNNECESSARY_LOGIN(HttpStatus.BAD_REQUEST, "❎ [ERROR] 이미 로그인된 상태입니다. 추가 로그인은 필요하지 않습니다."),
    FAIL_UNNECESSARY_LOGOUT(HttpStatus.BAD_REQUEST, "❎ [ERROR] 로그아웃 상태입니다. 로그아웃을 시도할 필요가 없습니다."),


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
