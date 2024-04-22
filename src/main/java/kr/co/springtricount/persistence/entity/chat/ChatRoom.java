package kr.co.springtricount.persistence.entity.chat;

import static kr.co.springtricount.persistence.entity.chat.ChatRoomStatus.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.co.springtricount.persistence.entity.BaseEntity;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
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

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private Member sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private Member receiver;

	@Enumerated(value = EnumType.STRING)
	private ChatRoomStatus chatRoomStatus = ACTIVE;

	@OneToMany(
		fetch = FetchType.LAZY,
		cascade = CascadeType.REMOVE
	)
	private List<ChatMessage> chatMessages = new ArrayList<>();

	@Builder
	public ChatRoom(String name,
		Member sender,
		Member receiver,
		ChatRoomStatus chatRoomStatus,
		List<ChatMessage> chatMessages) {
		this.name = name;
		this.sender = sender;
		this.receiver = receiver;
		this.chatRoomStatus = chatRoomStatus;
		this.chatMessages = chatMessages;
	}

	public static ChatRoom createChatRoom(Member sender,
		Member receiver,
		ChatRoomReqDTO chatRoomReqDTO) {

		return ChatRoom.builder()
			.name(chatRoomReqDTO.name())
			.sender(sender)
			.receiver(receiver)
			.build();
	}
}
