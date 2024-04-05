package kr.co.springtricount.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDTO(
        Long id,
        String name,
        Long settlementId,
        MemberDTO payerMember,
        BigDecimal amount,
        LocalDate expenseDate
) { }
