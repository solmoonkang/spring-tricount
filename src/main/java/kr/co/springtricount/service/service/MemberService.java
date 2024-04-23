package kr.co.springtricount.service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.springtricount.infra.error.exception.DuplicatedException;
import kr.co.springtricount.infra.error.exception.NotFoundException;
import kr.co.springtricount.infra.error.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.service.dto.request.SignupReqDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void createMember(SignupReqDTO signupReqDTO) {

		checkIdentityExists(signupReqDTO.identity());

		final Member member = Member.createMember(
			signupReqDTO,
			passwordEncoder.encode(signupReqDTO.password())
		);

		memberRepository.save(member);
	}

	public List<MemberResDTO> findAllMember() {

		final List<Member> findMember = memberRepository.findAll();

		return findMember.stream()
			.map(member -> new MemberResDTO(
				member.getId(),
				member.getIdentity(),
				member.getName()))
			.collect(Collectors.toList());
	}

	public MemberResDTO findMemberByIdentity(String identity) {

		final Member findMember = memberRepository.findMemberByIdentity(identity)
			.orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

		return new MemberResDTO(
			findMember.getId(),
			findMember.getIdentity(),
			findMember.getName()
		);
	}

	@Transactional
	public void deleteMember(Long memberId) {

		final Member deleteMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

		memberRepository.delete(deleteMember);
	}

	private void checkIdentityExists(String identity) {

		if (memberRepository.existsMemberByIdentity(identity)) {
			throw new DuplicatedException(ResponseStatus.FAIL_IDENTITY_DUPLICATION);
		}
	}
}
