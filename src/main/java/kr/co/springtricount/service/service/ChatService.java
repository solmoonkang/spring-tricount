package kr.co.springtricount.service.service;

import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import kr.co.springtricount.service.dto.response.ChatRoomResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void createChatRoom(ChatRoomReqDTO chatRoomReqDTO) {

        final ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomReqDTO.name())
                .build();

        chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoomResDTO> findAllChatRooms() {

        List<ChatRoom> findChatRoom = chatRoomRepository.findAll();

        return findChatRoom.stream()
                .map(chatRoom -> new ChatRoomResDTO(chatRoom.getName()))
                .collect(Collectors.toList());
    }
}
