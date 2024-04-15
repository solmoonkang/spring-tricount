package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
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

    private final ChatMessageService chatMessageService;

    @Transactional
    public void createChatRoom(ChatRoomReqDTO chatRoomReqDTO) {

        final ChatRoom chatRoom = ChatRoom.toChatRoomEntity(chatRoomReqDTO.name());

        chatRoomRepository.save(chatRoom);
    }

    public ChatRoomResDTO enterChatRoom(User currentMember, Long chatRoomId) {

        final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUNT));

        List<ChatMessageResDTO> messages =
                chatMessageService.findAllChatMessagesByChatRoomId(currentMember, chatRoomId);

        return new ChatRoomResDTO(findChatRoom.getName(), messages);
    }

    // TODO: 현재 로그인 한 사용자가 참여한 모든 채팅방을 조회한다.
    // 추가적인 요구사항은 채팅방들을 조회할 때, 각 채팅방에서 이루어진 채팅 메시지가 있다면 가장 최근 메시지를 각 채팅방과 함께 출력하도록 구현하자.
    public List<ChatRoomResDTO> findAllChatRoomsByMemberIdentity(User currentMember) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final List<ChatRoom> chatRooms = chatRoomRepository.findAllByMember(findMember);

        return chatRooms.stream()
                .map(chatRoom -> new ChatRoomResDTO(
                        chatRoom.getName(),
                        null
                ))
                .toList();
    }

    @Transactional
    public void deleteChatRoom(User currentMember, Long chatRoomId) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        chatRoomRepository.findAllByMember(findMember).stream()
                .filter(chatRoom -> chatRoom.getId().equals(chatRoomId))
                .findFirst()
                .ifPresent(chatRoomRepository::delete);
    }
}
