package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.MemberSettlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSettlementRepository extends JpaRepository<MemberSettlement, Long> {
}
