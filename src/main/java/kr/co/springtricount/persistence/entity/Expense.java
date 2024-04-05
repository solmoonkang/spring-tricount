package kr.co.springtricount.persistence.entity;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.BaseEntity;
import kr.co.springtricount.service.dto.ExpenseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "tbl_expenses")
@AttributeOverride(
        name = "id",
        column = @Column(name = "expense_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense extends BaseEntity {

    @Column(name = "name", length = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "settlement_id")
    private Settlement settlement;

    @Column(name = "amount")
    private BigDecimal amount = new BigDecimal("123456.789");

    @Column(name = "expense_date")
    private LocalDate expenseDate;

    @Builder
    public Expense(String name,
                   Member member,
                   Settlement settlement,
                   BigDecimal amount,
                   LocalDate expenseDate) {
        this.name = name;
        this.member = member;
        this.settlement = settlement;
        this.amount = amount.setScale(0, RoundingMode.HALF_UP);
        this.expenseDate = expenseDate;
    }

    public static Expense toExpenseEntity(ExpenseDTO create,
                                          Member member,
                                          Settlement settlement) {

        return Expense.builder()
                .name(create.name())
                .member(member)
                .settlement(settlement)
                .amount(create.amount())
                .expenseDate(create.expenseDate())
                .build();
    }
}
