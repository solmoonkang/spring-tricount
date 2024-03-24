package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.MemberSettlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSettlementRepository extends JpaRepository<MemberSettlement, Long> {

    List<MemberSettlement> findAllByMemberIdentity(String memberIdentity);

    boolean existsBySettlementIdAndMemberIdentity(Long settlementId, String memberIdentity);

    List<MemberSettlement> findAllBySettlementId(Long memberSettlementId);
}
