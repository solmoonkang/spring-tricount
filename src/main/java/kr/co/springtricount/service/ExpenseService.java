package kr.co.springtricount.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.Expense;
import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.entity.MemberSettlement;
import kr.co.springtricount.persistence.entity.Settlement;
import kr.co.springtricount.persistence.repository.ExpenseRepository;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.persistence.repository.MemberSettlementRepository;
import kr.co.springtricount.persistence.repository.SettlementRepository;
import kr.co.springtricount.service.dto.request.ExpenseReqDTO;
import kr.co.springtricount.service.dto.response.ExpenseResDTO;
import lombok.RequiredArgsConstructor;
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
    public void createExpense(ExpenseReqDTO create, String memberLoginIdentity) {

        final List<MemberSettlement> memberSettlements =
                memberSettlementRepository.findAllByMemberIdentity(memberLoginIdentity);

        checkMemberParticipationInSettlements(memberSettlements);

        final Settlement settlement = settlementRepository.findById(create.settlementId())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND));

        final Member member = memberRepository.findMemberByIdentity(create.memberIdentity())
                .orElseThrow(() -> new NotFoundException("입력한 회원 아이디는 정산에 참여한 회원 아이디와 일치하지 않습니다."));

        final Expense expense = Expense.toExpenseEntity(create, member, settlement);

        expenseRepository.save(expense);
    }

    public List<ExpenseResDTO> findAllExpenses() {

        final List<Expense> expenses = expenseRepository.findAll();

        return expenses.stream().map(Expense::toExpenseResDTO).collect(Collectors.toList());
    }

    @Transactional
    public void deleteExpenseById(Long expenseId, String memberLoginIdentity) {

        final List<MemberSettlement> memberSettlements =
                memberSettlementRepository.findAllByMemberIdentity(memberLoginIdentity);

        checkMemberParticipationInSettlements(memberSettlements);

        expenseRepository.deleteById(expenseId);
    }

    private void checkMemberParticipationInSettlements(List<MemberSettlement> memberSettlements) {

        if (memberSettlements.isEmpty()) {
            throw new NotFoundException("참여한 정산이 없어서 지출 내역을 생성할 수 없습니다.");
        }
    }
}
