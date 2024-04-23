package kr.co.springtricount.service.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.springtricount.infra.error.exception.NotFoundException;
import kr.co.springtricount.infra.error.exception.UnauthorizedAccessException;
import kr.co.springtricount.infra.error.handler.WebSocketChatHandler;
import kr.co.springtricount.infra.error.response.ResponseStatus;
import kr.co.springtricount.infra.security.MemberDetailService;
import kr.co.springtricount.infra.utils.RedisKeyUtils;
import kr.co.springtricount.infra.utils.RedisUtils;
import kr.co.springtricount.persistence.entity.chat.ChatMessage;
import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.ChatMessageRepository;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.service.dto.request.ChatMessageReqDTO;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

	private final ChatRoomRepository chatRoomRepository;

	private final ChatMessageRepository chatMessageRepository;

	private final WebSocketChatHandler webSocketChatHandler;

	private final MemberDetailService memberDetailService;

	private final RedisTemplate<String, Object> redisTemplate;

	private final RedisUtils redisUtils;

	@Transactional
	public void sendAndSaveChatMessage(Long chatRoomId, ChatMessageReqDTO chatMessageReqDTO) {

		final Member findMember = memberDetailService.getLoggedInMember();

		final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUND));

		checkAccessPermission(findMember, findChatRoom);

		final ChatMessage chatMessage = ChatMessage.createChatMessage(findMember, findChatRoom, chatMessageReqDTO);

		chatMessageRepository.save(chatMessage);

		sendChatMessageViaWebSocket(chatRoomId, chatMessage);

		updateChatMessageInRedis(chatRoomId, chatMessage);
	}

	public List<ChatMessageResDTO> findAllChatMessagesByChatRoomId(Long chatRoomId) {

		final Member findMember = memberDetailService.getLoggedInMember();

		final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUND));

		checkAccessPermission(findMember, findChatRoom);

		final List<ChatMessage> chatMessages = chatMessageRepository.findChatMessagesByChatRoomId(chatRoomId);

		return chatMessages.stream()
			.map(this::convertToChatMessageResDTO)
			.toList();
	}

	private ChatMessageResDTO convertToChatMessageResDTO(ChatMessage chatMessage) {

		return new ChatMessageResDTO(
			chatMessage.getChatRoom().getId(),
			chatMessage.getSender().getName(),
			chatMessage.getMessage()
		);
	}

	private void checkAccessPermission(Member member, ChatRoom chatRoom) {

		if (!(chatRoom.getSender().equals(member) || chatRoom.getReceiver().equals(member))) {
			throw new UnauthorizedAccessException(ResponseStatus.FAIL_UNAUTHORIZED);
		}
	}

	private void updateChatMessageInRedis(Long chatRoomId, ChatMessage chatMessage) {

		final String redisKey = RedisKeyUtils.chatRoomKey(chatRoomId);

		redisUtils.addChatMessageToRedisList(redisKey, convertToChatMessageResDTO(chatMessage));
	}

	private void sendChatMessageViaWebSocket(Long chatRoomId, ChatMessage chatMessage) {

		webSocketChatHandler.sendMessageToChatRoom(
			chatRoomId,
			convertToChatMessageResDTO(chatMessage)
		);
	}
}
