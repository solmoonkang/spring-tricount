package kr.co.springtricount.persistence.entity.chat;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.entity.BaseEntity;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.service.dto.request.ChatMessageReqDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tbl_chat_messages")
@AttributeOverride(
        name = "id",
        column = @Column(name = "chat_message_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;

    @Column(name = "message")
    private String message;

    @Builder
    public ChatMessage(ChatRoom chatRoom,
                       Member sender,
                       String message) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.message = message;
    }

    public static ChatMessage toChatMessageEntity(Member sender,
                                                  ChatRoom chatRoom,
                                                  ChatMessageReqDTO chatMessageReqDTO) {

        return ChatMessage.builder()
                .sender(sender)
                .chatRoom(chatRoom)
                .message(chatMessageReqDTO.message())
                .build();
    }
}
