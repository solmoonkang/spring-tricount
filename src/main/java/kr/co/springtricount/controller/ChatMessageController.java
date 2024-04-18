package kr.co.springtricount.controller;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.dto.request.ChatMessageReqDTO;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
import kr.co.springtricount.service.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/{chat_room_id}")
    public ResponseFormat<Void> sendAndSaveChatMessage(@PathVariable("chat_room_id") Long chatRoomId,
                                                       @RequestBody @Validated ChatMessageReqDTO chatMessageReqDTO) {

        chatMessageService.sendAndSaveChatMessage(chatRoomId, chatMessageReqDTO);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
    }

    @GetMapping("/{chat_room_id}")
    public ResponseFormat<List<ChatMessageResDTO>> findAllChatMessagesByChatRoomId(
            @PathVariable("chat_room_id") Long chatRoomId) {

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                chatMessageService.findAllChatMessagesByChatRoomId(chatRoomId)
        );
    }
}
