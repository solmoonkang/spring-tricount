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
@Table(name = "tbl_chats")
@AttributeOverride(
        name = "id",
        column = @Column(name = "chat_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseEntity {

    @ManyToOne
    private Member member;

    @ManyToOne
    private ChatRoom chatRoom;

    @Column(name = "message")
    private String message;

    @Column(name = "message_type")
    private MessageType messageType;

    @Builder
    public Chat(Member member,
                ChatRoom chatRoom,
                String message,
                MessageType messageType) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.message = message;
        this.messageType = messageType;
    }
}
