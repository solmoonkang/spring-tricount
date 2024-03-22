package kr.co.springtricount.service.dto.response;

import java.util.List;

public record MemberSettlementDTO(
        String settlementName,
        List<String> memberNames
) { }
