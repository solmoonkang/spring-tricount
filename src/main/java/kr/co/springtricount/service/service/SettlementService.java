package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.member.Member;
import kr.co.springtricount.persistence.entity.member.MemberSettlement;
import kr.co.springtricount.persistence.entity.settlement.Settlement;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.persistence.repository.MemberSettlementRepository;
import kr.co.springtricount.persistence.repository.SettlementRepository;
import kr.co.springtricount.persistence.repository.search.SettlementSearchRepository;
import kr.co.springtricount.service.dto.request.SettlementReqDTO;
import kr.co.springtricount.service.dto.response.ExpenseResDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import kr.co.springtricount.service.dto.response.SettlementResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final SettlementRepository settlementRepository;

    private final MemberRepository memberRepository;

    private final MemberSettlementRepository memberSettlementRepository;

    private final ExpenseService expenseService;

    private final SettlementSearchRepository settlementSearchRepository;

    @Transactional
    public void createSettlement(User currentMember, SettlementReqDTO settlementReqDTO) {

        final Member loginMember = memberRepository.findMemberByIdentity(currentMember.getUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        final Settlement settlement = Settlement.toSettlementEntity(settlementReqDTO);

        final MemberSettlement memberSettlement =
                MemberSettlement.toMemberSettlementEntity(loginMember, settlement);

        settlementRepository.save(settlement);

        memberSettlementRepository.save(memberSettlement);
    }

    public SettlementResDTO findSettlementById(User currentMember, Long settlementId) {

        validateMemberParticipation(settlementId, currentMember.getUsername());

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
    public void deleteSettlementById(User currentMember, Long settlementId) {

        validateMemberParticipation(settlementId, currentMember.getUsername());

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

    private void validateMemberParticipation(Long settlementId, String currentMember) {

        if (!memberSettlementRepository.existsBySettlementIdAndMemberIdentity(settlementId, currentMember)) {
            throw new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND);
        }
    }

    private void validateSettlementNotEmpty(List<Settlement> settlements) {

        if (settlements.isEmpty()) {
            throw new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND);
        }
    }
}
