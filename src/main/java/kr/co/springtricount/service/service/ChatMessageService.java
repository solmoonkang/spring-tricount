package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.exception.UnauthorizedAccessException;
import kr.co.springtricount.infra.handler.WebSocketChatHandler;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.chat.ChatMessage;
import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.ChatMessageRepository;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.ChatMessageReqDTO;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final MemberRepository memberRepository;

    private final WebSocketChatHandler webSocketChatHandler;

    private final StringRedisTemplate stringRedisTemplate;

    @Transactional
    public void sendAndSaveChatMessage(User currentMember, Long chatRoomId, ChatMessageReqDTO chatMessageReqDTO) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUND));

        validateChatRoomAccess(findChatRoom, findMember);

        final ChatMessage chatMessage = ChatMessage.createChatMessage(findMember, findChatRoom, chatMessageReqDTO);

        chatMessageRepository.save(chatMessage);

        // Save Message in Redis
        String chatMessageKey = "chatRoom: " + chatRoomId + ": messages";
        stringRedisTemplate.opsForList().rightPush(chatMessageKey, chatMessage.getMessage());

        webSocketChatHandler.sendMessageToChatRoom(
                chatRoomId,
                toChatMessageResDTO(chatRoomId, findMember.getName(), chatMessage.getMessage())
        );
    }

    public List<ChatMessageResDTO> findAllChatMessagesByChatRoomId(User currentMember, Long chatRoomId) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUND));

        checkAccessPermission(findMember, findChatRoom);

        final List<ChatMessage> chatMessages = chatMessageRepository.findChatMessagesByChatRoomId(chatRoomId);

        return chatMessages.stream()
                .map(chatMessage -> toChatMessageResDTO(
                        findChatRoom.getId(),
                        chatMessage.getSender().getName(),
                        chatMessage.getMessage())
                )
                .toList();
    }

    private void validateChatRoomAccess(ChatRoom chatRoom, Member member) {

        if (!(chatRoom.getSender().equals(member) || chatRoom.getReceiver().equals(member))) {
            throw new UnauthorizedAccessException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }

    private ChatMessageResDTO toChatMessageResDTO(Long chatRoomId, String senderName, String message) {

        return new ChatMessageResDTO(chatRoomId, senderName, message);
    }

    private void checkAccessPermission(Member member, ChatRoom chatRoom) {

        if (!(chatRoom.getSender().equals(member) || chatRoom.getReceiver().equals(member))) {
            throw new UnauthorizedAccessException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }
}
