package kr.co.springtricount.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.springtricount.infra.error.response.ResponseFormat;
import kr.co.springtricount.infra.error.response.ResponseStatus;
import kr.co.springtricount.service.dto.response.BalanceResDTO;
import kr.co.springtricount.service.dto.response.SettlementResDTO;
import kr.co.springtricount.service.service.BalanceService;
import kr.co.springtricount.service.service.SettlementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/balances")
public class BalanceController {

	private final BalanceService balanceService;

	private final SettlementService settlementService;

	@GetMapping("/{settlement_id}")
	public ResponseFormat<List<BalanceResDTO>> findBalanceBySettlement(
		@PathVariable("settlement_id") Long settlementId) {

		SettlementResDTO settlement = settlementService.findSettlementById(settlementId);

		return ResponseFormat.successMessageWithData(
			ResponseStatus.SUCCESS_EXECUTE,
			balanceService.findBalanceBySettlement(settlement)
		);
	}
}
