package kr.co.springtricount.service.service;

import kr.co.springtricount.infra.exception.NotFoundException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.dto.response.BalanceResDTO;
import kr.co.springtricount.service.dto.response.ExpenseResDTO;
import kr.co.springtricount.service.dto.response.MemberResDTO;
import kr.co.springtricount.service.dto.response.SettlementResDTO;
import lombok.With;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BalanceService {

    public List<BalanceResDTO> findBalanceBySettlement(SettlementResDTO settlement) {

        if (settlement == null) {
            throw new NotFoundException(ResponseStatus.FAIL_SETTLEMENT_NOT_FOUND);
        }

        List<ExpenseResDTO> expenses = settlement.expenses();
        List<MemberResDTO> participants = settlement.participants();

        BigDecimal totalAmount = getTotalAmount(expenses);
        BigDecimal averageAmount = totalAmount.divide(BigDecimal.valueOf(participants.size()), RoundingMode.DOWN);

        Map<MemberResDTO, BigDecimal> memberExpenseMap = getMemberExpenseMap(expenses);
        Map<MemberResDTO, BigDecimal> memberAmountMap = getMemberAmountMap(participants, memberExpenseMap, averageAmount);

        List<Balance> receiver = getMemberBigDecimals(
                memberAmountMap,
                entry -> entry.getValue().compareTo(BigDecimal.ZERO) > 0,
                Comparator.comparing(Balance::amount));

        List<Balance> sender = getMemberBigDecimals(
                memberAmountMap,
                entry -> entry.getValue().compareTo(BigDecimal.ZERO) < 0,
                Comparator.comparing(Balance::amount).reversed());

        return getBalances(receiver, sender);
    }

    private List<BalanceResDTO> getBalances(List<Balance> receiver, List<Balance> sender) {

        int receiverIndex = 0;
        int senderIndex = 0;

        List<BalanceResDTO> balances = new ArrayList<>();

        Balance receiverMember = receiver.get(0);
        Balance senderMember = sender.get(0);

        while (true) {
            BigDecimal receiverAmount = receiverMember.amount().abs();
            BigDecimal senderAmount = senderMember.amount().abs();

            BigDecimal minAmount = receiverAmount.min(senderAmount);

            receiverMember = receiverMember.withAmount(receiverMember.amount().subtract(minAmount));
            senderMember = senderMember.withAmount(senderMember.amount().add(minAmount));

            balances.add(
                    new BalanceResDTO(
                            senderMember.member().id(),
                            senderMember.member().name(),
                            minAmount.abs().longValue(),
                            receiverMember.member().id(),
                            receiverMember.member().name()));

            if (receiverMember.amount().compareTo(BigDecimal.ZERO) == 0) {
                receiverIndex++;
                if (receiverIndex >= receiver.size()) break;
                receiverMember = receiver.get(receiverIndex);
            }

            if (senderMember.amount().compareTo(BigDecimal.ZERO) == 0) {
                senderIndex++;
                if (senderIndex >= sender.size()) break;
                senderMember = receiver.get(senderIndex);
            }
        }

        return balances;
    }

    private static List<Balance> getMemberBigDecimals(
            Map<MemberResDTO, BigDecimal> memberAmountMap,
            Predicate<Map.Entry<MemberResDTO, BigDecimal>> entryPredicate,
            Comparator<Balance> comparing) {

        return memberAmountMap.entrySet().stream()
                .filter(entryPredicate)
                .map(entry -> new Balance(entry.getKey(), entry.getValue()))
                .sorted(comparing)
                .toList();
    }

    private Map<MemberResDTO, BigDecimal> getMemberAmountMap(
            List<MemberResDTO> participants,
            Map<MemberResDTO, BigDecimal> memberExpenseMap,
            BigDecimal averageAmount) {

        return participants.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        member -> memberExpenseMap.getOrDefault(member, BigDecimal.ZERO).subtract(averageAmount)
                        ));
    }

    private Map<MemberResDTO, BigDecimal> getMemberExpenseMap(List<ExpenseResDTO> expenses) {

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        ExpenseResDTO::payerMember,
                        Collectors.reducing(BigDecimal.ZERO, ExpenseResDTO::amount, BigDecimal::add)
                        ));
    }

    private BigDecimal getTotalAmount(List<ExpenseResDTO> expenses) {

        return expenses.stream()
                .map(ExpenseResDTO::amount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private record Balance(
            MemberResDTO member,
            @With BigDecimal amount
    ) { }
}
