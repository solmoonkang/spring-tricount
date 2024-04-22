package kr.co.springtricount.persistence.entity.member;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.springtricount.persistence.entity.BaseEntity;
import kr.co.springtricount.persistence.entity.settlement.Settlement;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
	name = "tbl_member_settlements",
	indexes = {
		@Index(name = "idx_member_id", columnList = "member_id"),
		@Index(name = "idx_settlement_id", columnList = "settlement_id")
	}
)
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

	public static MemberSettlement createMemberSettlement(Member member,
		Settlement settlement) {

		return MemberSettlement.builder()
			.member(member)
			.settlement(settlement)
			.build();
	}
}
