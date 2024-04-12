package kr.co.springtricount.controller;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import kr.co.springtricount.service.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_rooms")
public class ChatRoomController {

    private ChatRoomService chatRoomService;

    @PostMapping
    public ResponseFormat<Void> createChatRoom(@RequestBody @Validated ChatRoomReqDTO chatRoomReqDTO) {

        chatRoomService.createChatRoom(chatRoomReqDTO);

        return ResponseFormat.successMessage(
                ResponseStatus.SUCCESS_CREATED
        );
    }
}
