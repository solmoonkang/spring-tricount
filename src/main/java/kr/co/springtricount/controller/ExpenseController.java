package kr.co.springtricount.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.infra.config.SessionConstant;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.ExpenseService;
import kr.co.springtricount.service.dto.request.ExpenseReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/{settlement_id}")
    public ResponseFormat<Void> createExpense(@PathVariable(name = "settlement_id") Long settlementId,
                                              @RequestBody @Validated ExpenseReqDTO create,
                                              HttpSession httpSession) {

        final String memberLoginIdentity = (String) httpSession.getAttribute(SessionConstant.LOGIN_MEMBER);

        expenseService.createExpense(settlementId, create, memberLoginIdentity);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
    }

    @DeleteMapping("/{expense_id}")
    public ResponseFormat<Void> deleteExpense(@PathVariable(name = "expense_id") Long expenseId,
                                              HttpSession httpSession) {

        final String memberLoginIdentity = (String) httpSession.getAttribute(SessionConstant.LOGIN_MEMBER);

        expenseService.deleteExpenseById(expenseId, memberLoginIdentity);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
