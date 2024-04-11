package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
