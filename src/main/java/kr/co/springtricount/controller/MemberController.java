package kr.co.springtricount.controller;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.dto.request.SignupDTO;
import kr.co.springtricount.service.service.MemberService;
import kr.co.springtricount.service.dto.MemberResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseFormat<Void> createMember(@Validated @RequestBody SignupDTO signupDTO) {

        memberService.createMember(signupDTO);

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
