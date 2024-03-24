package kr.co.springtricount.service;

import kr.co.springtricount.infra.exception.InvalidRequestException;
import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.Expense;
import kr.co.springtricount.persistence.entity.MemberSettlement;
import kr.co.springtricount.persistence.repository.ExpenseRepository;
import kr.co.springtricount.persistence.repository.MemberSettlementRepository;
import kr.co.springtricount.service.dto.request.ExpenseReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final MemberSettlementRepository memberSettlementRepository;

    @Transactional
    public void createExpense(ExpenseReqDTO create, String memberLoginIdentity, Long settlementId) {

        final MemberSettlement memberSettlement = memberSettlementRepository
                .findByMemberIdentityAndSettlementId(memberLoginIdentity, settlementId)
                .stream().findFirst()
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_NOT_FOUND));

        checkMemberNameMatchesSettlementParticipant(memberSettlement, create.memberName());

        final Expense expense = Expense.toExpenseEntity(
                create,
                memberSettlement.getMember(),
                memberSettlement.getSettlement()
        );

        expenseRepository.save(expense);
    }

    private void checkMemberNameMatchesSettlementParticipant(MemberSettlement memberSettlement,
                                                             String memberName) {

        if (!memberSettlement.getMember().getName().equals(memberName)) {
            throw new InvalidRequestException("입력한 회원 이름이 정산에 참여한 회원의 이름과 일치하지 않습니다.");
        }
    }
}
