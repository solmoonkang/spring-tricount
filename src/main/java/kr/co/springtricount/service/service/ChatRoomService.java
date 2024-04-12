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

        final Member receiver = checkCurrentMemberByIdentity(chatRoomReqDTO.receiverIdentity());

        final ChatRoom chatRoom = ChatRoom.toChatRoomEntity(
                receiver,
                determineChatRoomName(chatRoomReqDTO, receiver)
        );

        chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoomResDTO> findAllChatRoomsByMemberIdentity(User currentMember) {

        final Member findMember = checkCurrentMemberByIdentity(currentMember.getUsername());

        final List<ChatRoom> chatRooms = chatRoomRepository.findAllByMember(findMember);

        return chatRooms.stream()
                .map(chatRoom -> new ChatRoomResDTO(
                        chatRoom.getName()
                ))
                .toList();
    }

    @Transactional
    public void deleteChatRoom(User currentMember, Long chatRoomId) {

        final Member findMember = checkCurrentMemberByIdentity(currentMember.getUsername());

        chatRoomRepository.findAllByMember(findMember).stream()
                .filter(chatRoom -> chatRoom.getId().equals(chatRoomId))
                .findFirst()
                .ifPresent(chatRoomRepository::delete);
    }

    private String determineChatRoomName(ChatRoomReqDTO chatRoomReqDTO, Member receiver) {

        if (!chatRoomReqDTO.chatRoomName().isBlank()) {
            return chatRoomReqDTO.chatRoomName();
        }

        return receiver.getName();
    }

    private Member checkCurrentMemberByIdentity(String memberIdentity) {

        return memberRepository.findMemberByIdentity(memberIdentity)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
    }
}
