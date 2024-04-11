package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.settlement.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
