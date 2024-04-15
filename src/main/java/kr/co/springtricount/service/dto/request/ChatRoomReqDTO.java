package kr.co.springtricount.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatRoomReqDTO(
        @NotBlank(message = "채팅방 이름을 입력해주세요.") String name
) { }
