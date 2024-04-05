package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.DuplicatedException;
import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.MemberResDTO;
import kr.co.springtricount.service.dto.request.SignupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createMember(SignupDTO signupDTO) {

        checkIdentityExists(signupDTO.identity());

        final Member member = Member.toMemberEntity(
                signupDTO, passwordEncoder.encode(signupDTO.password())
        );

        memberRepository.save(member);
    }

    public List<MemberResDTO> findAllMember() {

        final List<Member> findMember = memberRepository.findAll();

        return findMember.stream()
                .map(member -> new MemberResDTO(
                        member.getId(),
                        member.getIdentity(),
                        member.getName()))
                .collect(Collectors.toList());
    }

    public MemberResDTO findMemberByIdentity(String identity) {

        final Member findMember = memberRepository.findMemberByIdentity(identity)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        return new MemberResDTO(
                findMember.getId(),
                findMember.getIdentity(),
                findMember.getName()
        );
    }

    @Transactional
    public void deleteMember(Long memberId) {

        final Member deleteMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        memberRepository.delete(deleteMember);
    }

    private void checkIdentityExists(String identity) {

        if (memberRepository.existsMemberByIdentity(identity)) {
            throw new DuplicatedException(ResponseStatus.FAIL_IDENTITY_DUPLICATION);
        }
    }
}
