package kr.co.springtricount.service.dto.response;

public record ChatMessageResDTO(
	Long chatRoomId,
	String senderName,
	String message
) {
}
