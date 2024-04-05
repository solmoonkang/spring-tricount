package kr.co.springtricount.infra.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.service.dto.request.MemberReqDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static kr.co.springtricount.infra.config.SessionConstant.*;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession httpSession = request.getSession(false);

        if (httpSession != null &&
                httpSession.getAttribute(LOGIN_MEMBER) != null &&
                httpSession.getAttribute(LOGIN_MEMBER) instanceof MemberReqDTO) {

            return true;
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN);

        return false;
    }
}
