package kr.co.springtricount.service.dto.response;

import java.util.List;

public record ChatRoomResDTO(
        String name,
        List<ChatMessageResDTO> chatMessages
) { }
