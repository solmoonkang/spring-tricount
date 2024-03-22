package kr.co.springtricount.service;

import jakarta.servlet.http.HttpSession;
import kr.co.springtricount.infra.config.SessionConstant;
import kr.co.springtricount.infra.exception.AuthenticationException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.entity.MemberSettlement;
import kr.co.springtricount.persistence.entity.Settlement;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.persistence.repository.MemberSettlementRepository;
import kr.co.springtricount.persistence.repository.SettlementRepository;
import kr.co.springtricount.service.dto.request.SettlementReqDTO;
import kr.co.springtricount.service.dto.response.MemberSettlementResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    public List<MemberSettlementResDTO> findAllSettlementsByMember(HttpSession httpSession) {

        final String memberLoginIdentity = (String) httpSession.getAttribute(SessionConstant.LOGIN_MEMBER);

        final List<MemberSettlement> memberSettlements =
                memberSettlementRepository.findAllByMemberIdentity(memberLoginIdentity);

        checkMemberParticipation(memberSettlements, memberLoginIdentity);

        return convertToMemberSettlementResDTOs(memberSettlements);
    }

    private List<MemberSettlementResDTO> convertToMemberSettlementResDTOs(List<MemberSettlement> memberSettlements) {

        final Map<Settlement, List<MemberSettlement>> settlementsGroupedBySettlement = memberSettlements.stream()
                        .collect(Collectors.groupingBy(MemberSettlement::getSettlement));

        return settlementsGroupedBySettlement.entrySet().stream()
                .map(this::toMemberSettlementResDTO)
                .toList();
    }

    private MemberSettlementResDTO toMemberSettlementResDTO(Map.Entry<Settlement, List<MemberSettlement>> entry) {

        final Settlement settlement = entry.getKey();

        List<String> memberNames = entry.getValue().stream()
                .map(memberSettlement -> memberSettlement.getMember().getName())
                .toList();

        return new MemberSettlementResDTO(settlement.getName(), memberNames);
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
