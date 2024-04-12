package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.ChatMessageRepository;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import lombok.RequiredArgsConstructor;
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

        final ChatRoom chatRoom =
                ChatRoom.toChatRoomEntity(receiver, determineChatRoomName(chatRoomReqDTO, receiver));

        chatRoomRepository.save(chatRoom);
    }

    private String determineChatRoomName(ChatRoomReqDTO chatRoomReqDTO, Member receiver) {

        if (!chatRoomReqDTO.chatRoomName().isBlank()) {
            return chatRoomReqDTO.chatRoomName();
        }

        return receiver.getName();
    }
}
