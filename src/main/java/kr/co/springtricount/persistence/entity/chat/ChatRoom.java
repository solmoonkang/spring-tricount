package kr.co.springtricount.persistence.entity.chat;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.co.springtricount.persistence.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public ChatRoom(String name) {
        this.name = name;
    }
}
