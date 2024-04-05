package kr.co.springtricount.persistence.repository;

import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByIdentity(String identity);

    Optional<Member> findMemberByIdentity(String identity);

    List<MemberResDTO> findAllByIdentityIn(List<MemberResDTO> participants);
}
