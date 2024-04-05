package kr.co.springtricount.controller;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.dto.request.ExpenseReqDTO;
import kr.co.springtricount.service.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import com.sun.security.auth.UserPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseFormat<Void> createExpense(@AuthenticationPrincipal UserPrincipal currentMember,
                                              @RequestBody @Validated ExpenseReqDTO expenseReqDTO) {

        expenseService.createExpense(currentMember, expenseReqDTO);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
    }

    @DeleteMapping("/{expense_id}")
    public ResponseFormat<Void> deleteExpense(@AuthenticationPrincipal UserPrincipal currentMember,
                                              @PathVariable(name = "expense_id") Long expenseId) {

        expenseService.deleteExpenseById(currentMember, expenseId);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
