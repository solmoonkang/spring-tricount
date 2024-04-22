package kr.co.springtricount.infra.security;

public interface OAuth2MemberInfo {

	String getProviderId();

	String getProvider();

	String getEmail();

	String getName();
}
