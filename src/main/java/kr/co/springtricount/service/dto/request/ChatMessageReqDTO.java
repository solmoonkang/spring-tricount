package kr.co.springtricount.service.dto.request;

public record ChatMessageReqDTO(
        String sender,
        String message
) { }
