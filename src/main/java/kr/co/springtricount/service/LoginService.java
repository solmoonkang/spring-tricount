package kr.co.springtricount.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.MemberReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public void login(String identity, String password) {

        final Member member = memberRepository.findMemberByIdentity(identity)
                .filter(findMember -> findMember.getPassword().equals(password))
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        new MemberReqDTO(member.getIdentity(), member.getName(), null);
    }
}
