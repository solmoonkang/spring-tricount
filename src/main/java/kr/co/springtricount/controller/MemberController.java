package kr.co.springtricount.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.springtricount.infra.error.response.ResponseFormat;
import kr.co.springtricount.infra.error.response.ResponseStatus;
import kr.co.springtricount.service.dto.request.SignupReqDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import kr.co.springtricount.service.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseFormat<Void> createMember(@Validated @RequestBody SignupReqDTO signupReqDTO) {

		memberService.createMember(signupReqDTO);

		return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
	}

	@GetMapping
	public ResponseFormat<List<MemberResDTO>> findAllMember() {

		return ResponseFormat.successMessageWithData(
			ResponseStatus.SUCCESS_EXECUTE,
			memberService.findAllMember()
		);
	}

	@GetMapping("/{identity}")
	public ResponseFormat<MemberResDTO> findMemberByIdentity(@PathVariable("identity") String identity) {

		return ResponseFormat.successMessageWithData(
			ResponseStatus.SUCCESS_EXECUTE,
			memberService.findMemberByIdentity(identity)
		);
	}

	@DeleteMapping("/{member_id}")
	public ResponseFormat<Void> deleteMember(@PathVariable("member_id") Long memberId) {

		memberService.deleteMember(memberId);

		return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
	}
}
