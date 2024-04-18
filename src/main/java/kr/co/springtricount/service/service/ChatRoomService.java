package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.exception.UnauthorizedAccessException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.infra.security.MemberDetailService;
import kr.co.springtricount.persistence.entity.chat.ChatMessage;
import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.ChatMessageRepository;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.persistence.repository.search.ChatMessageSearchRepository;
import kr.co.springtricount.persistence.repository.search.ChatRoomSearchRepository;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
import kr.co.springtricount.service.dto.response.ChatRoomResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final MemberRepository memberRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomSearchRepository chatRoomSearchRepository;

    private final ChatMessageSearchRepository chatMessageSearchRepository;

    private final MemberDetailService memberDetailService;

    @Transactional
    public void createChatRoom(ChatRoomReqDTO chatRoomReqDTO) {

        final Member sender = memberDetailService.getLoggedInMember();

        final Member receiver = memberRepository.findMemberByIdentity(chatRoomReqDTO.receiverIdentity())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom chatRoom = ChatRoom.createChatRoom(sender, receiver, chatRoomReqDTO);

        chatRoomRepository.save(chatRoom);
    }

    public ChatRoomResDTO enterChatRoom(Long chatRoomId) {

        final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUND));

        final Member findMember = memberDetailService.getLoggedInMember();

        checkAccessPermission(findMember, findChatRoom);

        List<ChatMessageResDTO> messages =
                chatMessageSearchRepository.findAllMessageByChatRoomId(chatRoomId);

        return toChatRoomResDTO(findChatRoom, messages);
    }

    public List<ChatRoomResDTO> findAllChatRoomsByMemberIdentity() {

        final Member findMember = memberDetailService.getLoggedInMember();

        final List<ChatRoom> chatRooms =
                chatRoomSearchRepository.findAllChatRoomsByMemberWithMessages(findMember);

        return chatRooms.stream()
                .map(this::convertToChatRoomResDTO)
                .toList();
    }

    private ChatRoomResDTO convertToChatRoomResDTO(ChatRoom chatRoom) {

        final ChatMessageResDTO lastMessageResDTO = findLastMessage(chatRoom.getId());

        return toChatRoomResDTO(chatRoom, Collections.singletonList(lastMessageResDTO));
    }

    private ChatMessageResDTO findLastMessage(Long chatRoomId) {

        final ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedDateDesc(chatRoomId)
                .orElse(null);

        if (lastMessage != null) {
            return new ChatMessageResDTO(chatRoomId, lastMessage.getSender().getName(), lastMessage.getMessage());
        }

        return null;
    }

    private void checkAccessPermission(Member member, ChatRoom chatRoom) {

        if (!(chatRoom.getSender().equals(member) || chatRoom.getReceiver().equals(member))) {
            throw new UnauthorizedAccessException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }

    private ChatRoomResDTO toChatRoomResDTO(ChatRoom chatRoom, List<ChatMessageResDTO> messages) {

        return new ChatRoomResDTO(
                chatRoom.getName(),
                chatRoom.getSender().getName(),
                chatRoom.getReceiver().getName(),
                messages
        );
    }
}
