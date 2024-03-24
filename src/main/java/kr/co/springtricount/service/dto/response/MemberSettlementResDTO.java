package kr.co.springtricount.service.dto.response;

import java.util.List;

public record MemberSettlementResDTO(
        String settlementName,
        List<String> memberNames
) { }
