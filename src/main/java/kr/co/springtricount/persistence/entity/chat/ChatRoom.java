package kr.co.springtricount.persistence.entity.chat;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Builder
    public ChatRoom(String name,
                    List<ChatMessage> chatMessages) {
        this.name = name;
        this.chatMessages = chatMessages;
    }

    public static ChatRoom toChatRoomEntity(String name) {

        return ChatRoom.builder()
                .name(name)
                .build();
    }
}
