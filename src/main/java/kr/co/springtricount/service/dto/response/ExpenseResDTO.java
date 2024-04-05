package kr.co.springtricount.service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResDTO(
        Long id,
        String name,
        Long settlementId,
        String payerMember,
        BigDecimal amount,
        LocalDate expenseDate
) { }
