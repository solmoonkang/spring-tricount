package kr.co.springtricount.service;

import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.infra.config.SessionConstant;
import kr.co.springtricount.infra.exception.AuthenticationException;
import kr.co.springtricount.infra.exception.LoginException;
import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public void login(HttpSession httpSession, LoginDTO loginDTO) {

        checkAlreadyLogin(httpSession);

        final Member findMember = memberRepository.findMemberByIdentity(loginDTO.identity())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        checkPasswordMatch(findMember.getPassword(), loginDTO.password());

        httpSession.setAttribute(SessionConstant.LOGIN_MEMBER, findMember.getIdentity());
    }

    public void logout(HttpSession httpSession) {

        checkNotLogin(httpSession);

        httpSession.invalidate();
    }

    private void checkPasswordMatch(String storedPassword, String inputPassword) {

        if (!storedPassword.equals(inputPassword)) {
            throw new AuthenticationException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }

    private void checkAlreadyLogin(HttpSession httpSession) {

        if (isLoggedIn(httpSession)) {
            throw new LoginException(ResponseStatus.FAIL_UNNECESSARY_LOGIN);
        }
    }

    private void checkNotLogin(HttpSession httpSession) {

        if (!isLoggedIn(httpSession)) {
            throw new LoginException(ResponseStatus.FAIL_UNNECESSARY_LOGOUT);
        }
    }

    private boolean isLoggedIn(HttpSession httpSession) {

        return httpSession.getAttribute(SessionConstant.LOGIN_MEMBER) != null;
    }
}
