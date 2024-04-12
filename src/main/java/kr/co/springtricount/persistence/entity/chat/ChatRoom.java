package kr.co.springtricount.persistence.entity.chat;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.entity.BaseEntity;
import kr.co.springtricount.persistence.entity.member.Member;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Builder
    public ChatRoom(String name,
                    Member member,
                    List<ChatMessage> chatMessages) {
        this.name = name;
        this.member = member;
        this.chatMessages = chatMessages;
    }

    public static ChatRoom toChatRoomEntity(Member receiver,
                                            String chatRoomName) {

        return ChatRoom.builder()
                .name(chatRoomName)
                .member(receiver)
                .build();
    }
}
