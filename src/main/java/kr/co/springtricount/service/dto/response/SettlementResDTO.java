package kr.co.springtricount.service.dto.response;

import java.util.List;

public record SettlementResDTO(
        Long id,
        String settlementName,
        List<MemberResDTO> participants,
        List<ExpenseResDTO> expenses
) { }
