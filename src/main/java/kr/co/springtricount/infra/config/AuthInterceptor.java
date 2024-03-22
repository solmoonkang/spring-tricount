package kr.co.springtricount.infra.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        return isMemberCreationRequest(request) || isUserLoggedIn(request, response);
    }

    private boolean isMemberCreationRequest(HttpServletRequest request) {

        return request.getMethod().equalsIgnoreCase("POST") &&
                request.getRequestURI().equalsIgnoreCase("/api/v1/members");
    }

    private boolean isUserLoggedIn(HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {

        if (request.getSession().getAttribute(SessionConstant.LOGIN_MEMBER) == null) {
            sendUnauthorizedError(response);
            return false;
        }

        return true;
    }

    private void sendUnauthorizedError(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
    }
}
