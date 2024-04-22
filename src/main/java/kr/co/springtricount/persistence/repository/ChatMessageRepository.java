package kr.co.springtricount.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.springtricount.persistence.entity.chat.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findChatMessagesByChatRoomId(Long chatRoomId);

	Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedDateDesc(Long chatRoomId);
}
