package kr.co.springtricount.infra.security;

import java.util.Map;

import kr.co.springtricount.persistence.entity.member.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoMemberInfo implements OAuth2MemberInfo {

	private final Map<String, Object> attributes;

	@Override
	public String getProviderId() {

		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");

		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getProvider() {

		return "kakao";
	}

	@Override
	public String getEmail() {

		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");

		return (String)kakaoAccount.get("email");
	}

	@Override
	public String getName() {

		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");

		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

		return (String)profile.get("nickname");
	}

	public Member toEntity() {

		return new Member(getId(), getName(), getEmail());
	}

	public static KakaoMemberInfo of(String registrationId, Map<String, Object> oAuth2UserAttributes) {

		oAuth2UserAttributes.put(registrationId, 0);

		return new KakaoMemberInfo(oAuth2UserAttributes);
	}
}
