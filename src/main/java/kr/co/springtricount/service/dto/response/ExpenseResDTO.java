package kr.co.springtricount.service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResDTO(
        String name,
        String memberName,
        String settlementName,
        BigDecimal amount,
        LocalDate expenseDate
) { }
