package kr.co.springtricount.service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.infra.security.MemberDetailService;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.entity.member.MemberSettlement;
import kr.co.springtricount.persistence.entity.settlement.Expense;
import kr.co.springtricount.persistence.entity.settlement.Settlement;
import kr.co.springtricount.persistence.repository.ExpenseRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.persistence.repository.MemberSettlementRepository;
import kr.co.springtricount.persistence.repository.SettlementRepository;
import kr.co.springtricount.service.dto.request.ExpenseReqDTO;
import kr.co.springtricount.service.dto.response.ExpenseResDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

	private final ExpenseRepository expenseRepository;

	private final MemberRepository memberRepository;

	private final SettlementRepository settlementRepository;

	private final MemberSettlementRepository memberSettlementRepository;

	private final MemberDetailService memberDetailService;

	@Transactional
	public void createExpense(ExpenseReqDTO expenseReqDTO) {

		isMemberParticipatingInSettlement(
			expenseReqDTO.settlementId(),
			memberDetailService.getLoggedInMember().getIdentity()
		);

		final Settlement settlement = settlementRepository.findById(expenseReqDTO.settlementId())
			.orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND));

		final Member member = memberRepository.findMemberByIdentity(expenseReqDTO.payerMember().identity())
			.orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

		final Expense expense = Expense.createExpense(expenseReqDTO, member, settlement);

		expenseRepository.save(expense);
	}

	public List<ExpenseResDTO> findAllExpenses() {

		final List<Expense> expenses = expenseRepository.findAll();

		return expenses.stream()
			.map(expense -> new ExpenseResDTO(
				expense.getId(),
				expense.getName(),
				expense.getSettlement().getId(),
				new MemberResDTO(
					expense.getMember().getId(),
					expense.getMember().getIdentity(),
					expense.getMember().getName()
				),
				expense.getAmount(),
				expense.getExpenseDate()))
			.collect(Collectors.toList());
	}

	@Transactional
	public void deleteExpenseById(Long expenseId) {

		final List<MemberSettlement> memberSettlements = memberSettlementRepository
			.findAllByMemberIdentity(memberDetailService.getLoggedInMember().getIdentity());

		checkMemberParticipationInSettlements(memberSettlements);

		final Expense expense = expenseRepository.findById(expenseId)
			.orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_EXPENSE_NOT_FOUND));

		expenseRepository.delete(expense);
	}

	private void checkMemberParticipationInSettlements(List<MemberSettlement> memberSettlements) {

		if (memberSettlements.isEmpty()) {
			throw new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND);
		}
	}

	private void isMemberParticipatingInSettlement(Long settlementId, String identity) {

		if (!memberSettlementRepository.existsBySettlementIdAndMemberIdentity(settlementId, identity)) {
			throw new NotFoundException(ResponseStatus.FAIL_IDENTITY_NOT_FOUND);
		}
	}
}
