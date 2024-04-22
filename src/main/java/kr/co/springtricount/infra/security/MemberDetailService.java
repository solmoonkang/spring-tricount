package kr.co.springtricount.infra.security;

import java.util.ArrayList;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.exception.UnauthorizedAccessException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberDetailService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return memberRepository.findMemberByIdentity(username)
			.map(this::createUserDetails)
			.orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
	}

	public Member getLoggedInMember() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			return memberRepository.findMemberByIdentity(authentication.getName())
				.orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
		}

		throw new UnauthorizedAccessException(ResponseStatus.FAIL_UNAUTHORIZED);
	}

	private UserDetails createUserDetails(Member member) {

		return User.builder()
			.username(member.getIdentity())
			.password(member.getPassword())
			.authorities(new ArrayList<>())
			.build();
	}
}
