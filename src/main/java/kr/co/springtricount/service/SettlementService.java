package kr.co.springtricount.service;

import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.entity.MemberSettlement;
import kr.co.springtricount.persistence.entity.Settlement;
import kr.co.springtricount.persistence.repository.MemberRepository;
import kr.co.springtricount.persistence.repository.MemberSettlementRepository;
import kr.co.springtricount.persistence.repository.SettlementRepository;
import kr.co.springtricount.service.dto.request.SettlementReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {


    /**
     * 정산을 생성하려면, 정산을 할 정산 이름과 정산에 참여할 회원의 아이디를 입력해야 한다.
     * 여기서 회원이 존재하지 않는다면 예외가 발생한다.
     */

    private final SettlementRepository settlementRepository;

    private final MemberRepository memberRepository;

    private final MemberSettlementRepository memberSettlementRepository;

    @Transactional
    public void createSettlement(SettlementReqDTO create) {

        final Settlement settlement = Settlement.toSettlementEntity(create);

        final List<Member> members = memberRepository.findAllByIdentityIn(create.membersIdentity());

        final List<MemberSettlement> memberSettlements = members.stream()
                        .map(member -> MemberSettlement.toMemberSettlementEntity(member, settlement))
                        .collect(Collectors.toList());

        settlementRepository.save(settlement);

        memberSettlementRepository.saveAll(memberSettlements);
    }
}
