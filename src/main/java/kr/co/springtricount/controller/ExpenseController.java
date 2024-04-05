package kr.co.springtricount.controller;

import kr.co.springtricount.annotation.Login;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.service.ExpenseService;
import kr.co.springtricount.service.dto.MemberDTO;
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
                                              @Login MemberDTO member) {

        expenseService.createExpense(settlementId, create, member.identity());

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
    }

    @DeleteMapping("/{expense_id}")
    public ResponseFormat<Void> deleteExpense(@PathVariable(name = "expense_id") Long expenseId,
                                              @Login MemberDTO member) {

        expenseService.deleteExpenseById(expenseId, member.identity());

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
