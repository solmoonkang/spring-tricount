package kr.co.springtricount.persistence.repository.search;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.springtricount.persistence.entity.QMember;
import kr.co.springtricount.persistence.entity.QMemberSettlement;
import kr.co.springtricount.persistence.entity.QSettlement;
import kr.co.springtricount.persistence.entity.Settlement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
