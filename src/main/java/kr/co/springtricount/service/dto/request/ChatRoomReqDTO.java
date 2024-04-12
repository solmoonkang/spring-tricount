package kr.co.springtricount.service.dto.request;

public record ChatRoomReqDTO(
        String chatRoomName,
        String receiverIdentity
) { }
