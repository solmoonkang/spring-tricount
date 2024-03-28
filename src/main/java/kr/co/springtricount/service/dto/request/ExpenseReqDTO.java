package kr.co.springtricount.service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseReqDTO(
        @NotBlank(message = "지출 이름을 입력해주세요.") String name,
        @NotBlank(message = "지출한 사람의 아이디를 입력해주세요.") String memberIdentity,
        @DecimalMin(value = "0") BigDecimal amount,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expenseDate
) { }
