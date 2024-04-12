package kr.co.springtricount.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatRoomReqDTO(
        String chatRoomName,
        @NotBlank String receiverIdentity
) { }
