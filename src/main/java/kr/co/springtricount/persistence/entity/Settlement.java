package kr.co.springtricount.persistence.entity;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.BaseEntity;
import kr.co.springtricount.service.dto.response.SettlementResDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tbl_settlements")
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


    public static Settlement toSettlementEntity(SettlementResDTO create) {

        return Settlement.builder()
                .name(create.settlementName())
                .build();
    }
}
