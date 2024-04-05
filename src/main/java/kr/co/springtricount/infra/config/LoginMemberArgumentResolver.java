package kr.co.springtricount.infra.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.annotation.Login;
import kr.co.springtricount.service.dto.MemberDTO;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static kr.co.springtricount.infra.config.SessionConstant.LOGIN_MEMBER;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        boolean hasMemberType = parameter.getParameterType().isAssignableFrom(MemberDTO.class);

        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request != null) {
            return resolveArgumentIfRequestIsNotNull(request);
        }

        return null;
    }

    private MemberDTO resolveArgumentIfRequestIsNotNull(HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (httpSession != null &&
                httpSession.getAttribute(LOGIN_MEMBER) != null &&
                httpSession.getAttribute(LOGIN_MEMBER) instanceof MemberDTO member) {

            return member;
        }

        return null;
    }
}
