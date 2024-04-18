package kr.co.springtricount.controller;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.dto.request.SettlementReqDTO;
import kr.co.springtricount.service.service.SettlementService;
import kr.co.springtricount.service.dto.response.SettlementResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseFormat<Void> createSettlement(@RequestBody @Validated SettlementReqDTO settlementReqDTO) {

        settlementService.createSettlement(settlementReqDTO);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
    }

    @GetMapping("/{settlement_id}")
    public ResponseFormat<SettlementResDTO> findSettlementById(@PathVariable("settlement_id") Long settlementId) {

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                settlementService.findSettlementById(settlementId)
        );
    }

    @DeleteMapping("/{settlement_id}")
    public ResponseFormat<Void> deleteSettlementById(@PathVariable("settlement_id") Long settlementId) {


        settlementService.deleteSettlementById(settlementId);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
