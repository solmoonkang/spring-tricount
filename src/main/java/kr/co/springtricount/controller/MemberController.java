package kr.co.springtricount.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.springtricount.service.MemberService;
import kr.co.springtricount.service.dto.request.LoginDTO;
import kr.co.springtricount.service.dto.request.MemberReqDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody @Validated MemberReqDTO create) {

        memberService.createMember(create);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request,
                                      @RequestBody @Validated LoginDTO loginDTO) {

        memberService.login(request, loginDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        memberService.logout(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<MemberResDTO>> findAllMember() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.findAllMember());
    }

    @GetMapping("/{identity}")
    public ResponseEntity<MemberResDTO> findMemberByIdentity(@PathVariable String identity) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.findMemberByIdentity(identity));
    }

    @DeleteMapping("/{identity}")
    public ResponseEntity<Void> deleteMember(@PathVariable String identity) {

        memberService.deleteMember(identity);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
