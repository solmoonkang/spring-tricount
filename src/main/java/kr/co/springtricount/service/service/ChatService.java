package kr.co.springtricount.service.service;

import kr.co.springtricount.persistence.entity.chat.ChatRoom;
import kr.co.springtricount.persistence.repository.ChatRoomRepository;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
