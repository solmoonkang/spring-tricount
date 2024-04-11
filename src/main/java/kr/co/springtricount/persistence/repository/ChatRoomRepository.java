package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
