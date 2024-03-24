package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.MemberSettlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberSettlementRepository extends JpaRepository<MemberSettlement, Long> {

    List<MemberSettlement> findAllByMemberIdentity(String memberIdentity);

    boolean existsBySettlementIdAndMemberIdentity(Long settlementId, String memberIdentity);

    List<MemberSettlement> findAllBySettlementId(Long settlementId);

    Optional<MemberSettlement> findByMemberIdentityAndSettlementId(String memberIdentity, Long settlementId);
}
