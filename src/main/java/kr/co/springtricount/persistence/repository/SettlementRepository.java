package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.settlement.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
