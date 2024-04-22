package kr.co.springtricount.persistence.repository.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.co.springtricount.persistence.entity.member.QMember;
import kr.co.springtricount.persistence.entity.member.QMemberSettlement;
import kr.co.springtricount.persistence.entity.settlement.QExpense;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import kr.co.springtricount.service.service.BalanceService;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BalanceSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QMember member = QMember.member;

	private final QMemberSettlement memberSettlement = QMemberSettlement.memberSettlement;

	private final QExpense expense = QExpense.expense;

	public List<BalanceService.Balance> findBalancesBySettlementId(Long settlementId) {

		List<Tuple> results = jpaQueryFactory
			.select(member.id, member.identity, member.name, expense.amount.sum())
			.from(memberSettlement)
			.join(memberSettlement.member, member)
			.leftJoin(expense).on(expense.settlement.id.eq(memberSettlement.settlement.id))
			.where(memberSettlement.settlement.id.eq(settlementId))
			.groupBy(member.id)
			.fetch();

		// 결과 처리를 단순화
		return results.stream()
			.map(tuple -> new BalanceService.Balance(
				new MemberResDTO(
					tuple.get(member.id),
					tuple.get(member.identity),
					tuple.get(member.name)),
				tuple.get(expense.amount.sum())
			))
			.collect(Collectors.toList());
	}
}
