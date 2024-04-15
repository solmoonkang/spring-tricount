package kr.co.springtricount.persistence.entity.chat;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.entity.BaseEntity;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
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
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Builder
    public ChatRoom(String name,
                    Member sender,
                    Member receiver,
                    List<ChatMessage> chatMessages) {
        this.name = name;
        this.sender = sender;
        this.receiver = receiver;
        this.chatMessages = chatMessages;
    }

    public static ChatRoom toChatRoomEntity(Member sender,
                                            Member receiver,
                                            ChatRoomReqDTO chatRoomReqDTO) {

        return ChatRoom.builder()
                .name(chatRoomReqDTO.name())
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
