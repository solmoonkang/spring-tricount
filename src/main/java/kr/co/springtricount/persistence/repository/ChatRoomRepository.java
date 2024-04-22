package kr.co.springtricount.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	List<ChatRoom> findBySenderOrReceiver(Member sender, Member receiver);
}
