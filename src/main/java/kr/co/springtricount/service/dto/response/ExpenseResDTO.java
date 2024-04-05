package kr.co.springtricount.service.dto.response;

import kr.co.springtricount.service.dto.response.MemberResDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResDTO(
        Long id,
        String name,
        Long settlementId,
        MemberResDTO payerMember,
        BigDecimal amount,
        LocalDate expenseDate
) { }
