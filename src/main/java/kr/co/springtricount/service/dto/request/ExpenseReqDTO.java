package kr.co.springtricount.service.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.springtricount.service.dto.response.MemberResDTO;

public record ExpenseReqDTO(
	@NotBlank(message = "지출 내역 이름을 입력해주세요.") String expenseName,
	@NotNull(message = "참여하실 정산 번호를 입력해주세요.") Long settlementId,
	@NotNull(message = "지출 사용자의 이름을 입력해주세요.") MemberResDTO payerMember,
	@NotNull(message = "지출한 금액을 입력해주세요.") BigDecimal amount,
	@NotNull(message = "지출한 날짜를 입력해주세요.") @DateTimeFormat(pattern = "yy-MM-dd") LocalDate expenseDate
) {
}
