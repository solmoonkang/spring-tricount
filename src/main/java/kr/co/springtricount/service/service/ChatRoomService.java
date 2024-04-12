package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import kr.co.springtricount.service.dto.response.ChatRoomResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

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

    public List<ChatRoomResDTO> findAllChatRoomsByMemberIdentity(User currentMember) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final List<ChatRoom> chatRooms = chatRoomRepository.findAllByMember(findMember);

        return chatRooms.stream()
                .map(chatRoom -> new ChatRoomResDTO(
                        chatRoom.getName()
                ))
                .toList();
    }

    private String determineChatRoomName(ChatRoomReqDTO chatRoomReqDTO, Member receiver) {

        if (!chatRoomReqDTO.chatRoomName().isBlank()) {
            return chatRoomReqDTO.chatRoomName();
        }

        return receiver.getName();
    }
}
