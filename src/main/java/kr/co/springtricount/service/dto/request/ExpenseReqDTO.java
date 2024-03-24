package kr.co.springtricount.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseReqDTO(
        @NotBlank(message = "지출 이름을 입력해주세요.") String name,
        @NotBlank(message = "지출한 사람을 입력해주세요.") String memberName,
        @NotNull(message = "지출 금액을 입력해주세요.") BigDecimal amount,
        @NotBlank(message = "지출 날짜를 입력해주세요.") LocalDateTime expenseDate
) { }
