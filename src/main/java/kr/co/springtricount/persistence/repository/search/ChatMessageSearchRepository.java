package kr.co.springtricount.persistence.repository.search;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.co.springtricount.persistence.entity.chat.QChatMessage;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatMessageSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QChatMessage chatMessage = QChatMessage.chatMessage;

	public List<ChatMessageResDTO> findAllMessageByChatRoomId(Long chatRoomId) {

		return jpaQueryFactory
			.select(Projections.fields(ChatMessageResDTO.class,
				chatMessage.id.as("chatMessageId"),
				chatMessage.sender.name.as("chatMessageSender"),
				chatMessage.message).as("chatMessage")
			)
			.from(chatMessage)
			.where(chatMessage.chatRoom.id.eq(chatRoomId))
			.fetch();
	}
}
