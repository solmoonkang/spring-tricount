package kr.co.springtricount.service.dto.response;

public record BalanceResDTO(
        Long senderMemberNo,
        String senderMemberName,
        String sendAmount,
        Long receiverMemberNo,
        String receiverMemberName
) { }
