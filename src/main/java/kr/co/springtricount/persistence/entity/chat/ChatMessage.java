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
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Column(name = "message")
    private String message;

    @Column(name = "message_type")
    private MessageType messageType;

    @Column(name = "is_read")
    private boolean isRead;

    @Builder
    public ChatMessage(Member member,
                       ChatRoom chatRoom,
                       String message,
                       MessageType messageType,
                       boolean isRead) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.message = message;
        this.messageType = messageType;
        this.isRead = isRead;
    }
}
