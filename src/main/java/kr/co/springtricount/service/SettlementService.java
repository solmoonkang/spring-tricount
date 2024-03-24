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

import java.util.*;
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

    public List<MemberSettlementResDTO> findAllSettlementsByMember(String memberLoginIdentity) {

        final List<MemberSettlement> memberSettlements =
                memberSettlementRepository.findAllByMemberIdentity(memberLoginIdentity);

        checkSettlementsNotEmpty(memberSettlements);

        final List<MemberSettlement> allMemberSettlements = findAllMemberSettlementsForMember(memberSettlements);

        return convertToMemberSettlementResDTOs(allMemberSettlements);
    }

    @Transactional
    public void deleteSettlementById(Long settlementId, String memberLoginIdentity) {

        final List<MemberSettlement> memberSettlements =
                memberSettlementRepository.findAllByMemberIdentity(memberLoginIdentity);

        checkMemberParticipation(settlementId, memberLoginIdentity);

        settlementRepository.deleteById(settlementId);

        memberSettlementRepository.deleteAll(memberSettlements);
    }

    private List<MemberSettlementResDTO> convertToMemberSettlementResDTOs(List<MemberSettlement> memberSettlements) {

        return memberSettlements.stream()
                .collect(Collectors.groupingBy(MemberSettlement::getSettlement))
                .entrySet().stream()
                .map(this::toMemberSettlementResDTO)
                .collect(Collectors.toList());
    }

    private MemberSettlementResDTO toMemberSettlementResDTO(Map.Entry<Settlement, List<MemberSettlement>> entry) {

        List<String> memberNames = entry.getValue().stream()
                .map(memberSettlement -> memberSettlement.getMember().getName())
                .toList();

        return new MemberSettlementResDTO(entry.getKey().getName(), memberNames);
    }

    private List<MemberSettlement> findAllMemberSettlementsForMember(List<MemberSettlement> initialMemberSettlements) {

        return initialMemberSettlements.stream()
                .map(memberSettlement -> memberSettlement.getSettlement().getId())
                .flatMap(id -> memberSettlementRepository.findAllBySettlementId(id).stream())
                .toList();
    }

    private void checkSettlementsNotEmpty(List<MemberSettlement> memberSettlements) {

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
