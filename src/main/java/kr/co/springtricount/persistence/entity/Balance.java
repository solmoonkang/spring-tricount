package kr.co.springtricount.persistence.entity;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_balances")
@AttributeOverride(
        name = "id",
        column = @Column(name = "balance_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Balance extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "settlement_id")
    private Settlement settlement;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "average_amount")
    private BigDecimal averageAmount;

    @Builder
    public Balance(Settlement settlement,
                   BigDecimal totalAmount,
                   BigDecimal averageAmount) {
        this.settlement = settlement;
        this.totalAmount = totalAmount;
        this.averageAmount = averageAmount;
    }
}
