package kr.co.springtricount.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.springtricount.infra.error.response.ResponseFormat;
import kr.co.springtricount.infra.error.response.ResponseStatus;
import kr.co.springtricount.service.dto.request.ExpenseReqDTO;
import kr.co.springtricount.service.service.ExpenseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

	private final ExpenseService expenseService;

	@PostMapping
	public ResponseFormat<Void> createExpense(@RequestBody @Validated ExpenseReqDTO expenseReqDTO) {

		expenseService.createExpense(expenseReqDTO);

		return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
	}

	@DeleteMapping("/{expense_id}")
	public ResponseFormat<Void> deleteExpense(@PathVariable(name = "expense_id") Long expenseId) {

		expenseService.deleteExpenseById(expenseId);

		return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
	}
}
