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
    public void createChatRoom(ChatRoomReqDTO chatRoomReqDTO) {

        final Member receiver = memberRepository.findMemberByIdentity(chatRoomReqDTO.receiverIdentity())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom chatRoom = ChatRoom.toChatRoomEntity(
                receiver,
                determineChatRoomName(chatRoomReqDTO, receiver)
        );

        chatRoomRepository.save(chatRoom);
    }

    /**
     * createChatMessage: 채팅 메시지 생성 로직
     * 채팅 메시지 전송 시 채팅방 안에서 전송이 이루어진다. 따라서 채팅 메시지는 채팅방 ID를 가지고 있다.
     * 만약, 채팅 메시지가 갖는 채팅방 ID가 이미 생성된 채팅방이라면 그냥 메시지를 전송하면 된다.
     * 반대로, 채팅 메시지를 보낼 때 채팅방 ID가 없는 채팅방이라면 새로운 채팅방을 생성하고, 메시지를 전송한다.
     */
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

    private ChatRoom createOrGetChatRoom(Long chatRoomId, Member receiver) {

        return chatRoomRepository.findById(chatRoomId)
                .orElseGet(() -> createChatRoom(receiver));
    }

    private ChatRoom createChatRoom(Member receiver) {

        return chatRoomRepository.save(ChatRoom.toChatRoomEntity(receiver, receiver.getName()));
    }

    private String determineChatRoomName(ChatRoomReqDTO chatRoomReqDTO, Member receiver) {

        if (!chatRoomReqDTO.chatRoomName().isBlank()) {
            return chatRoomReqDTO.chatRoomName();
        }

        return receiver.getName();
    }
}
