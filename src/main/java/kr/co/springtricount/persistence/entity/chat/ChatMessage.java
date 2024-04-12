package kr.co.springtricount.persistence.entity.chat;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.entity.BaseEntity;
import kr.co.springtricount.persistence.entity.member.Member;
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
    @JoinColumn(name = "member_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Column(name = "message")
    private String message;

    @Builder
    public ChatMessage(Member sender,
                       ChatRoom chatRoom,
                       String message) {
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.message = message;
    }
}
