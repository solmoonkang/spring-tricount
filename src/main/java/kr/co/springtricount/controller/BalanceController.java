package kr.co.springtricount.controller;

import kr.co.springtricount.annotation.Login;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.BalanceService;
import kr.co.springtricount.service.SettlementService;
import kr.co.springtricount.service.dto.BalanceDTO;
import kr.co.springtricount.service.dto.MemberDTO;
import kr.co.springtricount.service.dto.SettlementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/balances")
public class BalanceController {

    private final BalanceService balanceService;

    private final SettlementService settlementService;

    @GetMapping("/{settlement_id}")
    public ResponseFormat<List<BalanceDTO>> findBalanceBySettlement(@Login MemberDTO member,
                                                                    @PathVariable("settlement_id") Long settlementId) {

        SettlementDTO settlement = settlementService.findSettlementById(member, settlementId);

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                balanceService.findBalanceBySettlement(settlement)
        );
    }
}
