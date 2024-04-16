package kr.co.springtricount.persistence.entity.member;

import jakarta.persistence.*;
import kr.co.springtricount.persistence.entity.BaseEntity;
import kr.co.springtricount.service.dto.SignupDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tbl_members")
@AttributeOverride(
        name = "id",
        column = @Column(name = "member_id", length = 4)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(name = "identity", length = 50, nullable = false, unique = true)
    private String identity;

    @Column(name = "password", nullable = false)
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

    public static Member createMember(SignupDTO signupDTO,
                                      String encodePassword) {

        return Member.builder()
                .identity(signupDTO.identity())
                .password(encodePassword)
                .name(signupDTO.name())
                .build();
    }
}
