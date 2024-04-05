package kr.co.springtricount.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SettlementReqDTO(
        @NotBlank(message = "정산 이름을 입력해주세요.") String settlementName
) { }
