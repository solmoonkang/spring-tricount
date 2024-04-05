package kr.co.springtricount.controller;

import kr.co.springtricount.annotation.Login;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.service.BalanceService;
import kr.co.springtricount.service.service.SettlementService;
import kr.co.springtricount.service.dto.response.BalanceResDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import kr.co.springtricount.service.dto.response.SettlementResDTO;
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
    public ResponseFormat<List<BalanceResDTO>> findBalanceBySettlement(@Login MemberResDTO member,
                                                                       @PathVariable("settlement_id") Long settlementId) {

        SettlementResDTO settlement = settlementService.findSettlementById(member, settlementId);

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                balanceService.findBalanceBySettlement(settlement)
        );
    }
}
