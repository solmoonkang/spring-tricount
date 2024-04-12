package kr.co.springtricount.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageReqDTO(
        String message,
        @NotBlank String receiverIdentity
) { }
