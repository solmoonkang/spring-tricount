package kr.co.springtricount.persistence.repository.search;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.chat.QChatMessage;
import kr.co.springtricount.persistence.entity.chat.QChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.entity.member.QMember;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QChatRoom chatRoom = QChatRoom.chatRoom;

	private final QMember sender = new QMember("sender");

	private final QMember receiver = new QMember("receiver");

	private final QChatMessage chatMessage = QChatMessage.chatMessage;

	public List<ChatRoom> findAllChatRoomsByMemberWithMessages(Member member) {

		return jpaQueryFactory.selectFrom(chatRoom)
			.leftJoin(chatRoom.sender, sender).fetchJoin()
			.leftJoin(chatRoom.receiver, receiver).fetchJoin()
			.leftJoin(chatRoom.chatMessages, chatMessage).fetchJoin()
			.where(
				chatRoom.sender.eq(member)
					.or(chatRoom.receiver.eq(member))
			)
			.distinct()
			.fetch();
	}
}
