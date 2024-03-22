package kr.co.springtricount.service;

import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.infra.config.SessionConstant;
import kr.co.springtricount.infra.exception.AuthenticationException;
import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.entity.MemberSettlement;
import kr.co.springtricount.persistence.entity.Settlement;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.persistence.repository.MemberSettlementRepository;
import kr.co.springtricount.persistence.repository.SettlementRepository;
import kr.co.springtricount.service.dto.request.SettlementReqDTO;
import kr.co.springtricount.service.dto.response.MemberSettlementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final SettlementRepository settlementRepository;

    private final MemberRepository memberRepository;

    private final MemberSettlementRepository memberSettlementRepository;

    @Transactional
    public void createSettlement(SettlementReqDTO create) {

        final Settlement settlement = Settlement.toSettlementEntity(create);

        final List<Member> members = memberRepository.findAllByIdentityIn(create.memberIdentities());

        final List<MemberSettlement> memberSettlements = members.stream()
                        .map(member -> MemberSettlement.toMemberSettlementEntity(member, settlement))
                        .collect(Collectors.toList());

        settlementRepository.save(settlement);

        memberSettlementRepository.saveAll(memberSettlements);
    }

    public MemberSettlementDTO findSettlementById(Long settlementId, HttpSession httpSession) {

        final String memberLoginIdentity = (String) httpSession.getAttribute(SessionConstant.LOGIN_MEMBER);

        final List<MemberSettlement> memberSettlements =
                memberSettlementRepository.findAllBySettlementId(settlementId);

        checkSettlementsExistence(memberSettlements);

        final Settlement firstSettlement = memberSettlements.get(0).getSettlement();

        final List<String> memberNames = memberSettlements.stream()
                .map(memberSettlement -> memberSettlement.getMember().getName())
                .toList();

        checkMemberParticipation(memberSettlements, memberLoginIdentity);

        return MemberSettlement.toMemberSettlementReadDto(firstSettlement, memberNames);
    }

    private void checkSettlementsExistence(List<MemberSettlement> memberSettlements) {

        if (memberSettlements.isEmpty()) {
            throw new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND);
        }
    }

    private void checkMemberParticipation(List<MemberSettlement> memberSettlements,
                                          String memberLoginIdentity) {

        if (!checkIfMemberIsParticipating(memberSettlements, memberLoginIdentity)) {
            throw new AuthenticationException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }

    private boolean checkIfMemberIsParticipating(List<MemberSettlement> memberSettlements,
                                                 String memberLoginIdentity) {

        return memberSettlements.stream()
                .anyMatch(memberSettlement -> memberSettlement.getMember().getIdentity().equals(memberLoginIdentity));
    }
}
