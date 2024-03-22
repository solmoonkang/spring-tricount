package kr.co.springtricount.service;

import jakarta.servlet.http.HttpServletRequest;
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

    public void login(HttpServletRequest request, LoginDTO loginDTO) {

        checkAlreadyLogin(request);

        final Member findMember = memberRepository.findMemberByIdentity(loginDTO.identity())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        checkPasswordMatch(findMember.getPassword(), loginDTO.password());

        request.getSession().setAttribute(SessionConstant.LOGIN_MEMBER, findMember.getIdentity());
    }

    public void logout(HttpServletRequest request) {

        checkNotLogin(request);

        request.getSession().invalidate();
    }

    private void checkPasswordMatch(String storedPassword, String inputPassword) {
        if (!storedPassword.equals(inputPassword)) {
            throw new AuthenticationException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }

    private void checkAlreadyLogin(HttpServletRequest request) {
        if (isLoggedIn(request)) {
            throw new LoginException(ResponseStatus.FAIL_UNNECESSARY_LOGIN);
        }
    }

    private void checkNotLogin(HttpServletRequest request) {
        if (!isLoggedIn(request)) {
            throw new LoginException(ResponseStatus.FAIL_UNNECESSARY_LOGOUT);
        }
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        return request.getSession().getAttribute(SessionConstant.LOGIN_MEMBER) != null;
    }
}
