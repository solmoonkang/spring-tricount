package kr.co.springtricount.persistence.entity;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.BaseEntity;
import kr.co.springtricount.service.dto.request.SettlementReqDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tbl_settlements")
@AttributeOverride(
        name = "id",
        column = @Column(name = "settlement_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseEntity {

    private String name;

    @Builder
    public Settlement(String name) {
        this.name = name;
    }

    public static Settlement toSettlementEntity(SettlementReqDTO create) {

        return Settlement.builder()
                .name(create.settlementName())
                .build();
    }
}
