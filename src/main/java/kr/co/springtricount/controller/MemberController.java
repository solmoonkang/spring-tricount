package kr.co.springtricount.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.infra.config.SessionConstant;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.MemberService;
import kr.co.springtricount.service.dto.request.LoginDTO;
import kr.co.springtricount.service.dto.request.MemberReqDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseFormat<Void> createMember(@RequestBody @Validated MemberReqDTO create) {

        memberService.createMember(create);

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
    public ResponseFormat<MemberResDTO> findMemberByIdentity(@PathVariable String identity) {

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                memberService.findMemberByIdentity(identity)
        );
    }

    @DeleteMapping
    public ResponseFormat<Void> deleteMember(HttpSession httpSession,
                                             @RequestBody @Validated LoginDTO loginDTO) {

        String loggedInUserIdentity = (String) httpSession.getAttribute(SessionConstant.LOGIN_MEMBER);

        memberService.deleteMember(loggedInUserIdentity, loginDTO);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
