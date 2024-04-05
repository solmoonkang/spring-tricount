package kr.co.springtricount.service.dto;

import java.util.List;

public record SettlementDTO(
        Long id,
        String settlementName,
        List<MemberResDTO> participants,
        List<ExpenseDTO> expenses
) { }
