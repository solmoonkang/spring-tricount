package kr.co.springtricount.persistence.entity.settlement;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.entity.BaseEntity;
import kr.co.springtricount.service.dto.request.SettlementReqDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(
        name = "tbl_settlements",
        indexes = @Index(name = "idx_settlement_name", columnList = "name")
)
@AttributeOverride(
        name = "id",
        column = @Column(name = "settlement_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseEntity {

    @Column(name = "name")
    private String name;

    @OneToMany(
            mappedBy = "settlement",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    private List<Expense> expenses = new ArrayList<>();

    @Builder
    public Settlement(String name, List<Expense> expenses) {
        this.name = name;
        this.expenses = expenses;
    }


    public static Settlement toSettlementEntity(SettlementReqDTO settlementReqDTO) {

        return Settlement.builder()
                .name(settlementReqDTO.settlementName())
                .build();
    }
}
