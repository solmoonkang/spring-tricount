package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.chat.ChatMessage;
import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.chat.MessageType;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.ChatMessageRepository;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.ChatMessageReqDTO;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void createChatRoom(User currentMember, ChatRoomReqDTO chatRoomReqDTO) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomReqDTO.chatRoomName())
                .messageType(MessageType.ENTER)
                .member(findMember)
                .chatMessages(null)
                .build();

        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void createChatMessage(Long chatRoomId, ChatMessageReqDTO chatMessageReqDTO) {

        final Member findMember = memberRepository.findMemberByIdentity(chatMessageReqDTO.sender())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException("해당 채팅방은 존재하지 않습니다."));

        final ChatMessage chatMessage = ChatMessage.builder()
                .sender(findMember)
                .chatRoom(chatRoom)
                .message(chatMessageReqDTO.message())
                .build();

        chatMessageRepository.save(chatMessage);
    }
}
