package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.settlement.Expense;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.entity.member.MemberSettlement;
import kr.co.springtricount.persistence.entity.settlement.Settlement;
import kr.co.springtricount.persistence.repository.ExpenseRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.persistence.repository.MemberSettlementRepository;
import kr.co.springtricount.persistence.repository.SettlementRepository;
import kr.co.springtricount.service.dto.request.ExpenseReqDTO;
import kr.co.springtricount.service.dto.response.ExpenseResDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final MemberRepository memberRepository;

    private final SettlementRepository settlementRepository;

    private final MemberSettlementRepository memberSettlementRepository;

    @Transactional
    public void createExpense(User currentMember, ExpenseReqDTO expenseReqDTO) {

        isMemberParticipatingInSettlement(expenseReqDTO.settlementId(), currentMember.getUsername());

        final Settlement settlement = settlementRepository.findById(expenseReqDTO.settlementId())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND));

        final Member member = memberRepository.findMemberByIdentity(expenseReqDTO.payerMember().identity())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final Expense expense = Expense.toExpenseEntity(expenseReqDTO, member, settlement);

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
    public void deleteExpenseById(User currentMember, Long expenseId) {

        final List<MemberSettlement> memberSettlements =
                memberSettlementRepository.findAllByMemberIdentity(currentMember.getUsername());

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

        boolean isParticipatedIdentity = memberSettlementRepository.existsBySettlementIdAndMemberIdentity(settlementId, identity);

        if (!isParticipatedIdentity) {
            throw new NotFoundException(ResponseStatus.FAIL_IDENTITY_NOT_FOUND);
        }
    }
}
