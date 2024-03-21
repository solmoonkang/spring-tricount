package kr.co.springtricount.service;

import kr.co.springtricount.infra.exception.DuplicatedException;
import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.MemberReqDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void createMember(MemberReqDTO create) {

        checkIdentityExists(create.identity());

        final Member member = Member.toMemberEntity(create);

        memberRepository.save(member);
    }

    public List<MemberResDTO> findAllMember() {

        final List<Member> findMember = memberRepository.findAll();

        return findMember.stream()
                .map(Member::toReadDto)
                .collect(Collectors.toList());
    }

    public MemberResDTO findMemberByIdentity(String identity) {

        final Member findMember = memberRepository.findMemberByIdentity(identity)
                .orElseThrow();

        return findMember.toReadDto();
    }

    private void checkIdentityExists(String identity) {
        if (memberRepository.existsMemberByIdentity(identity)) {
            throw new DuplicatedException("해당 아이디는 이미 존재하는 아이디입니다.");
        }
    }

}
