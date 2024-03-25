package kr.co.springtricount.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.infra.config.SessionConstant;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.ExpenseService;
import kr.co.springtricount.service.dto.request.ExpenseReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseFormat<Void> createExpense(@RequestBody @Validated ExpenseReqDTO create,
                                              HttpSession httpSession) {

        final String memberLoginIdentity = (String) httpSession.getAttribute(SessionConstant.LOGIN_MEMBER);

        expenseService.createExpense(create, memberLoginIdentity);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
    }
}
