package kr.co.springtricount.infra.security;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		// 1. 유저 정보(attributes)를 가져온다.
		Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

		// 2. resistrationId를 가져온다. (third-party id)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// 3. userNameAttributeName을 가져온다.
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
			.getUserInfoEndpoint().getUserNameAttributeName();

		// 4. 유저 정보 DTO를 생성한다.
		KakaoMemberInfo kakaoMemberInfo = KakaoMemberInfo.of(registrationId, oAuth2UserAttributes);

		// 5. 회원가입 및 로그인을 진행한다.
		Member member = getOrSave(kakaoMemberInfo);

		// 6. OAuth2User를 반환한다.
		return new PrincipalDetails(member, oAuth2UserAttributes, userNameAttributeName);
	}

	private Member getOrSave(KakaoMemberInfo kakaoMemberInfo) {

		Member member = memberRepository.findMemberByIdentity(kakaoMemberInfo.id())
			.orElseGet(kakaoMemberInfo::toEntity);

		return memberRepository.save(member);
	}
}
