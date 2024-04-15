package kr.co.springtricount.service.dto.response;

import java.util.List;

public record ChatRoomResDTO(
        String name,
        String senderName,
        String receiverName,
        List<ChatMessageResDTO> chatMessages
) { }
