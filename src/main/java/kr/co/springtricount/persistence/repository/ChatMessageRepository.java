package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findChatMessagesByChatRoomId(Long chatRoomId);
}
