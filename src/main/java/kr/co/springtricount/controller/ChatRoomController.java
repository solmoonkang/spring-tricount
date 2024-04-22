package kr.co.springtricount.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.dto.request.ChatRoomReqDTO;
import kr.co.springtricount.service.dto.response.ChatRoomResDTO;
import kr.co.springtricount.service.service.ChatRoomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat_rooms")
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

	@PostMapping
	public ResponseFormat<Void> createChatRoom(@RequestBody @Validated ChatRoomReqDTO chatRoomReqDTO) {

		chatRoomService.createChatRoom(chatRoomReqDTO);

		return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
	}

	@GetMapping("/{chat_room_id}")
	public ResponseFormat<ChatRoomResDTO> enterChatRoom(@PathVariable("chat_room_id") Long chatRoomId) {

		return ResponseFormat.successMessageWithData(
			ResponseStatus.SUCCESS_EXECUTE,
			chatRoomService.enterChatRoom(chatRoomId)
		);
	}

	@GetMapping
	public ResponseFormat<List<ChatRoomResDTO>> findAllChatRoomsByMemberIdentity() {

		return ResponseFormat.successMessageWithData(
			ResponseStatus.SUCCESS_EXECUTE,
			chatRoomService.findAllChatRoomsByMemberIdentity()
		);
	}
}
