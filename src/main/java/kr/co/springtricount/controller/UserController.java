package kr.co.springtricount.controller;

import kr.co.springtricount.service.UserService;
import kr.co.springtricount.service.dto.request.MemberReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Validated MemberReqDTO create) {

        userService.createUser(create);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
