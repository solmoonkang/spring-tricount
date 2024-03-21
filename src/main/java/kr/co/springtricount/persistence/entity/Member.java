package kr.co.springtricount.persistence.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.co.springtricount.persistence.BaseEntity;
import kr.co.springtricount.service.dto.request.MemberReqDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_members")
@AttributeOverride(
        name = "id",
        column = @Column(name = "member_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(name = "identity", length = 50, nullable = false, unique = true)
    private String identity;

    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Builder
    public Member(String identity,
                  String password,
                  String name) {
        this.identity = identity;
        this.password = password;
        this.name = name;
    }

    public static Member toMemberEntity(MemberReqDTO create) {

        return Member.builder()
                .identity(create.identity())
                .password(create.password())
                .name(create.name())
                .build();
    }

    public MemberResDTO toReadDto() {

        return new MemberResDTO(identity, name);
    }
}
