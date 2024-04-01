package kr.co.springtricount.controller;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.BalanceService;
import kr.co.springtricount.service.dto.response.BalanceResDTO;
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

    @GetMapping("/{settlement_id}")
    public ResponseFormat<List<BalanceResDTO>> findBalanceBySettlement(@PathVariable(name = "settlement_id") Long settlementId) {

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                balanceService.findBalanceBySettlement(settlementId)
        );
    }
}
