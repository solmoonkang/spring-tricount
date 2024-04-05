package kr.co.springtricount.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.MemberService;
import kr.co.springtricount.service.dto.request.LoginDTO;
import kr.co.springtricount.service.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.co.springtricount.infra.config.SessionConstant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseFormat<Void> createMember(@Validated @RequestBody MemberDTO create) {

        memberService.createMember(create);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
    }

    @GetMapping
    public ResponseFormat<List<MemberDTO>> findAllMember() {

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                memberService.findAllMember()
        );
    }

    @GetMapping("/{identity}")
    public ResponseFormat<MemberDTO> findMemberByIdentity(@PathVariable String identity) {

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                memberService.findMemberByIdentity(identity)
        );
    }

    @DeleteMapping
    public ResponseFormat<Void> deleteMember(HttpSession httpSession,
                                             @RequestBody @Validated LoginDTO loginDTO) {

        String loggedInUserIdentity = (String) httpSession.getAttribute(LOGIN_MEMBER);

        memberService.deleteMember(loggedInUserIdentity, loginDTO);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
