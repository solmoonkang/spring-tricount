package kr.co.springtricount.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.springtricount.infra.exception.DuplicatedException;
import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.exception.UnauthorizedException;
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

    public void login(HttpServletRequest request, LoginDTO loginDTO) {

        final Member findMember = memberRepository.findMemberByIdentity(loginDTO.identity())
                .orElseThrow(() -> new NotFoundException("요청하신 회원은 없는 회원입니다."));

        checkPasswordMatch(findMember.getPassword(), loginDTO.password());

        request.getSession().setAttribute("member", findMember.getIdentity());
    }

    public void logout(HttpServletRequest request) {

        request.getSession().invalidate();
    }

    public List<MemberResDTO> findAllMember() {

        final List<Member> findMember = memberRepository.findAll();

        return findMember.stream()
                .map(Member::toReadDto)
                .collect(Collectors.toList());
    }

    public MemberResDTO findMemberByIdentity(String identity) {

        final Member findMember = memberRepository.findMemberByIdentity(identity)
                .orElseThrow(() -> new NotFoundException("요청하신 회원은 없는 회원입니다."));

        return findMember.toReadDto();
    }

    @Transactional
    public void deleteMember(String identity) {

        final Member deleteMember = memberRepository.findMemberByIdentity(identity)
                .orElseThrow(() -> new NotFoundException("요청하신 회원은 없는 회원입니다."));

        memberRepository.delete(deleteMember);
    }

    private void checkIdentityExists(String identity) {
        if (memberRepository.existsMemberByIdentity(identity)) {
            throw new DuplicatedException("해당 아이디는 이미 존재하는 아이디입니다.");
        }
    }

    private void checkPasswordMatch(String storedPassword, String inputPassword) {
        if (!storedPassword.equals(inputPassword)) {
            throw new UnauthorizedException("일치하지 않는 회원입니다.");
        }
    }
}
