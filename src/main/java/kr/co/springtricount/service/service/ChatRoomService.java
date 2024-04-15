package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.chat.ChatMessage;
import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.ChatMessageRepository;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
import kr.co.springtricount.service.dto.response.ChatRoomResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final MemberRepository memberRepository;

    private final ChatMessageService chatMessageService;

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void createChatRoom(User currentMember, ChatRoomReqDTO chatRoomReqDTO) {

        final Member member = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final ChatRoom chatRoom = ChatRoom.toChatRoomEntity(chatRoomReqDTO.name(), member);

        chatRoomRepository.save(chatRoom);
    }

    public ChatRoomResDTO enterChatRoom(User currentMember, Long chatRoomId) {

        final ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CHAT_ROOM_NOT_FOUNT));

        List<ChatMessageResDTO> messages =
                chatMessageService.findAllChatMessagesByChatRoomId(currentMember, chatRoomId);

        return new ChatRoomResDTO(findChatRoom.getName(), messages);
    }

    public List<ChatRoomResDTO> findAllChatRoomsByMemberIdentity(User currentMember) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomByMember(findMember);

        return chatRooms.stream()
                .map(this::convertToChatRoomResDTO)
                .toList();
    }

    @Transactional
    public void deleteChatRoom(User currentMember, Long chatRoomId) {

        final Member findMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        chatRoomRepository.findByIdAndMember(chatRoomId, findMember)
                .ifPresent(chatRoomRepository::delete);
    }

    private ChatRoomResDTO convertToChatRoomResDTO(ChatRoom chatRoom) {

        final ChatMessageResDTO lastMessageResDTO = findLastMessage(chatRoom.getId());

        return new ChatRoomResDTO(chatRoom.getName(), Collections.singletonList(lastMessageResDTO));
    }

    private ChatMessageResDTO findLastMessage(Long chatRoomId) {

        final ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedDateDesc(chatRoomId)
                .orElse(null);

        if (lastMessage != null) {
            return new ChatMessageResDTO(chatRoomId, lastMessage.getSender(), lastMessage.getMessage());
        }

        return null;
    }
}
