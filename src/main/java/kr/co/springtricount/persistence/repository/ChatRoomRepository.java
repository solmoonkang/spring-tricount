package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findChatRoomByMember(Member member);

    Optional<ChatRoom> findByIdAndMember(Long chatRoomId, Member member);
}
