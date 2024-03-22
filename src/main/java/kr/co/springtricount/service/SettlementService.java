package kr.co.springtricount.service;

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

    // TODO: 해당 정산에 참여한 회원만 정산 내역을 확인할 수 있는지 테스트
    // TODO: List<MemberSettlement>가 비었는지 검증하는 로직말고 또 다른 검증이 필요한지 고민
    public List<MemberSettlementResDTO> findAllSettlementsByMember(String memberLoginIdentity) {

        final List<MemberSettlement> memberSettlements =
                memberSettlementRepository.findAllByMemberIdentity(memberLoginIdentity);

        checkMemberExistence(memberSettlements);

        return convertToMemberSettlementResDTOs(memberSettlements);
    }

    // TODO: 해당 정산에 참여한 회원이 해당 정산 내역을 제거할 수 있는지 테스트
    @Transactional
    public void deleteSettlementById(Long settlementId, String memberLoginIdentity) {

        final List<MemberSettlement> memberSettlements =
                memberSettlementRepository.findAllByMemberIdentity(memberLoginIdentity);

        checkMemberParticipation(settlementId, memberLoginIdentity);

        settlementRepository.deleteById(settlementId);

        memberSettlementRepository.deleteAll(memberSettlements);
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

    private void checkMemberExistence(List<MemberSettlement> memberSettlements) {

        if (memberSettlements.isEmpty()) {
            throw new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND);
        }
    }

    private void checkMemberParticipation(Long settlementId, String memberLoginIdentity) {

        if (!memberSettlementRepository.existsBySettlementIdAndMemberIdentity(settlementId, memberLoginIdentity)) {
            throw new AuthenticationException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }
}
