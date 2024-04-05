package kr.co.springtricount.service.dto.response;

public record BalanceResDTO(
        Long senderMemberNo,
        String senderMemberName,
        Long senderAmount,
        Long receiverMemberNo,
        String receiverMemberName
) {
}
