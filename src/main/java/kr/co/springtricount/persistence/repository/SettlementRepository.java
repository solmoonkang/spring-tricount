package kr.co.springtricount.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.springtricount.persistence.entity.settlement.Settlement;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
