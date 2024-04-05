package kr.co.springtricount.controller;

import kr.co.springtricount.annotation.Login;
import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.service.SettlementService;
import kr.co.springtricount.service.dto.MemberDTO;
import kr.co.springtricount.service.dto.SettlementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseFormat<Void> createSettlement(@RequestBody @Validated SettlementDTO create) {

        settlementService.createSettlement(create);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
    }

    @GetMapping
    public ResponseFormat<List<SettlementDTO>> findAllSettlementsByMember(@Login MemberDTO member) {

        return ResponseFormat.successMessageWithData(
                ResponseStatus.SUCCESS_EXECUTE,
                settlementService.findAllSettlementsByMember(member.identity())
        );
    }

    @DeleteMapping("/{settlement_id}")
    public ResponseFormat<Void> deleteSettlementById(@PathVariable(name = "settlement_id") Long settlementId,
                                                     @Login MemberDTO member) {


        settlementService.deleteSettlementById(settlementId, member.identity());

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_EXECUTE);
    }
}
