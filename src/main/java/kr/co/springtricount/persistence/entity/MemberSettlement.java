package kr.co.springtricount.persistence.entity;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.BaseEntity;
import kr.co.springtricount.service.dto.response.MemberSettlementDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "tbl_member_settlements")
@AttributeOverride(
        name = "id",
        column = @Column(name = "member_settlement_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSettlement extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "settlement_id")
    private Settlement settlement;

    @Builder
    public MemberSettlement(Member member,
                            Settlement settlement) {
        this.member = member;
        this.settlement = settlement;
    }

    public static MemberSettlement toMemberSettlementEntity(Member member,
                                                            Settlement settlement) {

        return MemberSettlement.builder()
                .member(member)
                .settlement(settlement)
                .build();
    }

    public static MemberSettlementDTO toMemberSettlementReadDto(Settlement settlement,
                                                                List<String> memberNames) {

        return new MemberSettlementDTO(settlement.getName(), memberNames);
    }
}
