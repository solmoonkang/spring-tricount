package kr.co.springtricount.service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.infra.security.MemberDetailService;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.entity.member.MemberSettlement;
import kr.co.springtricount.persistence.entity.settlement.Settlement;
import kr.co.springtricount.persistence.repository.MemberSettlementRepository;
import kr.co.springtricount.persistence.repository.SettlementRepository;
import kr.co.springtricount.persistence.repository.search.SettlementSearchRepository;
import kr.co.springtricount.service.dto.request.SettlementReqDTO;
import kr.co.springtricount.service.dto.response.ExpenseResDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import kr.co.springtricount.service.dto.response.SettlementResDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

	private final SettlementRepository settlementRepository;

	private final MemberSettlementRepository memberSettlementRepository;

	private final ExpenseService expenseService;

	private final SettlementSearchRepository settlementSearchRepository;

	private final MemberDetailService memberDetailService;

	@Transactional
	public void createSettlement(SettlementReqDTO settlementReqDTO) {

		final Member loginMember = memberDetailService.getLoggedInMember();

		final Settlement settlement = Settlement.createSettlement(settlementReqDTO);

		final MemberSettlement memberSettlement =
			MemberSettlement.createMemberSettlement(loginMember, settlement);

		settlementRepository.save(settlement);

		memberSettlementRepository.save(memberSettlement);
	}

	public SettlementResDTO findSettlementById(Long settlementId) {

		validateMemberParticipation(settlementId, memberDetailService.getLoggedInMember().getIdentity());

		List<Settlement> settlements =
			settlementSearchRepository.findSettlementDetailsById(settlementId);

		validateSettlementNotEmpty(settlements);

		final List<MemberSettlement> memberSettlement =
			memberSettlementRepository.findAllBySettlementId(settlementId);

		final List<MemberResDTO> participants = toMemberResDTOFromMemberSettlement(memberSettlement);

		final List<ExpenseResDTO> expenses = expenseService.findAllExpenses();

		return new SettlementResDTO(
			settlements.get(0).getId(),
			settlements.get(0).getName(),
			participants,
			expenses
		);
	}

	@Transactional
	public void deleteSettlementById(Long settlementId) {

		validateMemberParticipation(settlementId, memberDetailService.getLoggedInMember().getIdentity());

		List<MemberSettlement> memberSettlements =
			memberSettlementRepository.findAllBySettlementId(settlementId);

		settlementRepository.deleteById(settlementId);

		memberSettlementRepository.deleteAll(memberSettlements);
	}

	private List<MemberResDTO> toMemberResDTOFromMemberSettlement(List<MemberSettlement> memberSettlements) {

		return memberSettlements.stream()
			.map(memberSettlement -> new MemberResDTO(
				memberSettlement.getMember().getId(),
				memberSettlement.getMember().getIdentity(),
				memberSettlement.getMember().getName()))
			.collect(Collectors.toList());
	}

	private void validateMemberParticipation(Long settlementId, String memberIdentity) {

		if (!memberSettlementRepository.existsBySettlementIdAndMemberIdentity(settlementId, memberIdentity)) {
			throw new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND);
		}
	}

	private void validateSettlementNotEmpty(List<Settlement> settlements) {

		if (settlements.isEmpty()) {
			throw new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND);
		}
	}
}
