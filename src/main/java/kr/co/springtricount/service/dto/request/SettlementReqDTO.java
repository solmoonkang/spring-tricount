package kr.co.springtricount.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SettlementReqDTO(
        @NotBlank(message = "정산 이름을 입력해주세요.") String settlementName,
        @NotEmpty(message = "정산에 참여할 회원의 아이디를 입력해주세요.") List<String> memberIdentities
) { }
