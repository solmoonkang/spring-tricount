package kr.co.springtricount.controller;

import kr.co.springtricount.infra.response.ResponseFormat;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.SettlementService;
import kr.co.springtricount.service.dto.request.SettlementReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseFormat<Void> createSettlement(@RequestBody @Validated SettlementReqDTO create) {

        settlementService.createSettlement(create);

        return ResponseFormat.successMessage(ResponseStatus.SUCCESS_CREATED);
    }
}
