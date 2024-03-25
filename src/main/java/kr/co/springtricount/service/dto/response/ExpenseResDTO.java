package kr.co.springtricount.service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResDTO(
        String name,
        String memberName,
        String settlementName,
        BigDecimal amount,
        LocalDateTime expenseDate
) { }
