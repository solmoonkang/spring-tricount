package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
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

    @Transactional
    public void createChatMessage(User currentMember, Long chatRoomId, ChatMessageReqDTO chatMessageReqDTO) {

        final Member sender = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final Member receiver = memberRepository.findMemberByIdentity(chatMessageReqDTO.receiverIdentity())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom chatRoom = createOrGetChatRoom(chatRoomId, receiver);

        final ChatMessage chatMessage = ChatMessage.toChatMessageEntity(sender, chatRoom, chatMessageReqDTO);

        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageResDTO> findAllChatMessagesByChatRoomId(User currentMember, Long chatRoomId) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getPassword())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUNT));

        final List<ChatMessage> chatMessages = chatMessageRepository.findChatMessagesByChatRoomId(chatRoomId);

        return chatMessages.stream()
                .map(chatMessage -> new ChatMessageResDTO(
                        findChatRoom.getId(),
                        findMember.getName(),
                        chatMessage.getMessage()
                ))
                .toList();
    }

    private ChatRoom createOrGetChatRoom(Long chatRoomId, Member receiver) {

        return chatRoomRepository.findById(chatRoomId)
                .orElseGet(() -> createChatRoom(receiver));
    }

    private ChatRoom createChatRoom(Member receiver) {

        return chatRoomRepository.save(ChatRoom.toChatRoomEntity(receiver, receiver.getName()));
    }
}
