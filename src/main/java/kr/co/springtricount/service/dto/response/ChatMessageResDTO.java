package kr.co.springtricount.service.dto.response;

import kr.co.springtricount.persistence.entity.chat.MessageType;

public record ChatMessageResDTO(

        MessageType messageType,
        Long chatRoomId,
        Long senderId,
        String message
) { }
