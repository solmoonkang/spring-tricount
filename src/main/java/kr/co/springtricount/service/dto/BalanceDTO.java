package kr.co.springtricount.service.dto;

public record BalanceDTO(
        Long senderMemberNo,
        String senderMemberName,
        Long senderAmount,
        Long receiverMemberNo,
        String receiverMemberName
) {
}
