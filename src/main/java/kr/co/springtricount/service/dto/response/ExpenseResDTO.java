package kr.co.springtricount.service.dto.response;

import java.time.LocalDate;

public record ExpenseResDTO(
        String name,
        String memberName,
        String settlementName,
        String amount,
        LocalDate expenseDate
) { }
