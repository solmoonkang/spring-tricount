package kr.co.springtricount.service.dto;

import java.util.List;

public record SettlementDTO(
        Long id,
        String settlementName,
        List<MemberDTO> participants,
        List<ExpenseDTO> expenses
) { }
