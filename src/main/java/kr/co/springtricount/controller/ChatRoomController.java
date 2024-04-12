package kr.co.springtricount.controller;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import kr.co.springtricount.service.dto.response.ChatRoomResDTO;
import kr.co.springtricount.service.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseFormat<Void> createChatRoom(@RequestBody @Validated ChatRoomReqDTO chatRoomReqDTO) {

        chatRoomService.createChatRoom(chatRoomReqDTO);

        return ResponseFormat.successMessage(
                ResponseStatus.SUCCESS_CREATED
        );
    }

    @GetMapping
    public ResponseFormat<List<ChatRoomResDTO>> findAllChatRoomsByMemberIdentity(@AuthenticationPrincipal User currentMember) {

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                chatRoomService.findAllChatRoomsByMemberIdentity(currentMember)
        );
    }

    @DeleteMapping("/{chat_room_id}")
    public ResponseFormat<Void> deleteChatRoom(@AuthenticationPrincipal User currentMember,
                                               @PathVariable("chat_room_id") Long chatRoomId) {

        chatRoomService.deleteChatRoom(currentMember, chatRoomId);

        return ResponseFormat.successMessage(
                ResponseStatus.SUCCESS_EXECUTE
        );
    }
}
