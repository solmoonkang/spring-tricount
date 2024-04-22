package kr.co.springtricount.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.springtricount.persistence.entity.member.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsMemberByIdentity(String identity);

	Optional<Member> findMemberByIdentity(String identity);
}
