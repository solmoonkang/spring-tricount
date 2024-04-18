package kr.co.springtricount.persistence.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.springtricount.persistence.entity.chat.QChatMessage;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
