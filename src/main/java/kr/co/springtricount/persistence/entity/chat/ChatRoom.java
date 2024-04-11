package kr.co.springtricount.persistence.entity.chat;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tbl_chat_rooms")
@AttributeOverride(
        name = "id",
        column = @Column(name = "chat_room_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "message_type")
    private MessageType messageType;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Builder
    public ChatRoom(String name,
                    MessageType messageType,
                    List<ChatMessage> chatMessages) {
        this.name = name;
        this.messageType = messageType;
        this.chatMessages = chatMessages;
    }
}
