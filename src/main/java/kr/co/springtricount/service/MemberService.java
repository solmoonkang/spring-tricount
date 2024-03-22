package kr.co.springtricount.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.springtricount.infra.config.SessionConstant;
import kr.co.springtricount.infra.exception.AuthenticationException;
import kr.co.springtricount.infra.exception.DuplicatedException;
import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.LoginDTO;
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
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        return findMember.toReadDto();
    }

    @Transactional
    public void deleteMember(HttpServletRequest request, LoginDTO loginDTO) {

        checkMemberLoginIdentityMatches(request, loginDTO.identity());

        final Member deleteMember = memberRepository.findMemberByIdentity(loginDTO.identity())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        checkPasswordMatch(deleteMember.getPassword(), loginDTO.password());

        memberRepository.delete(deleteMember);
    }

    private void checkIdentityExists(String identity) {
        if (memberRepository.existsMemberByIdentity(identity)) {
            throw new DuplicatedException(ResponseStatus.FAIL_IDENTITY_DUPLICATION);
        }
    }

    private void checkMemberLoginIdentityMatches(HttpServletRequest request, String identity) {
        String loggedInUserIdentity = (String) request.getSession().getAttribute(SessionConstant.LOGIN_MEMBER);

        if (!identity.equals(loggedInUserIdentity)) {
            throw new AuthenticationException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }

    private void checkPasswordMatch(String storedPassword, String inputPassword) {
        if (!storedPassword.equals(inputPassword)) {
            throw new AuthenticationException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }
}
