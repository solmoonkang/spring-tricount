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

    @Transactional
    public void sendAndSaveChatMessage(User currentMember, Long chatRoomId, ChatMessageReqDTO chatMessageReqDTO) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUND));

        validateChatRoomAccess(findChatRoom, findMember);

        final ChatMessage chatMessage = ChatMessage.toChatMessageEntity(findMember, findChatRoom, chatMessageReqDTO);

        chatMessageRepository.save(chatMessage);

        webSocketChatHandler.sendMessageToChatRoom(
                chatRoomId,
                chatMessage.toChatMessageResDTO(chatRoomId, findMember.getName(), chatMessage.getMessage())
        );
    }

    public List<ChatMessageResDTO> findAllChatMessagesByChatRoomId(User currentMember, Long chatRoomId) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUND));

        final List<ChatMessage> chatMessages = chatMessageRepository.findChatMessagesByChatRoomId(chatRoomId);

        return chatMessages.stream()
                .map(chatMessage -> chatMessage.toChatMessageResDTO(
                        findChatRoom.getId(),
                        findMember.getName(),
                        chatMessage.getMessage())
                )
                .toList();
    }

    private void validateChatRoomAccess(ChatRoom chatRoom, Member member) {

        if (!(chatRoom.getSender().equals(member) || chatRoom.getReceiver().equals(member))) {
            throw new UnauthorizedAccessException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }
}
