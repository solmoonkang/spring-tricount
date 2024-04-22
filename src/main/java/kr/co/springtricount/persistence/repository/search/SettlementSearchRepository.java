package kr.co.springtricount.persistence.repository.search;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.co.springtricount.persistence.entity.member.QMember;
import kr.co.springtricount.persistence.entity.member.QMemberSettlement;
import kr.co.springtricount.persistence.entity.settlement.QSettlement;
import kr.co.springtricount.persistence.entity.settlement.Settlement;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SettlementSearchRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QSettlement settlement = QSettlement.settlement;

	private final QMemberSettlement memberSettlement = QMemberSettlement.memberSettlement;

	private final QMember member = QMember.member;

	public List<Settlement> findSettlementDetailsById(Long settlementId) {

		return jpaQueryFactory
			.select(memberSettlement.settlement)
			.from(memberSettlement)
			.join(memberSettlement.settlement, settlement)
			.join(memberSettlement.member, member)
			.where(memberSettlement.settlement.id.eq(settlementId))
			.distinct()
			.fetch();
	}
}
