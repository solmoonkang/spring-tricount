package kr.co.springtricount.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.springtricount.persistence.entity.member.MemberSettlement;

public interface MemberSettlementRepository extends JpaRepository<MemberSettlement, Long> {

	List<MemberSettlement> findAllByMemberIdentity(String memberIdentity);

	boolean existsBySettlementIdAndMemberIdentity(Long settlementId, String memberIdentity);

	List<MemberSettlement> findAllBySettlementId(Long settlementId);
}
