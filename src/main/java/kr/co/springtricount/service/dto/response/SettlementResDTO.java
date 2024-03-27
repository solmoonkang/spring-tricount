package kr.co.springtricount.service.dto.response;

import java.util.List;

public record SettlementResDTO(
        String settlementName,
        List<String> memberNames,
        List<ExpenseResDTO> expenseList
) { }
