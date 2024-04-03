package kr.co.springtricount.service;

import kr.co.springtricount.persistence.entity.Expense;
import kr.co.springtricount.persistence.entity.Member;
import kr.co.springtricount.persistence.repository.ExpenseRepository;
import kr.co.springtricount.service.dto.response.BalanceResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BalanceService {

    private final ExpenseRepository expenseRepository;

    public List<BalanceResDTO> findBalanceBySettlement(Long settlementId) {

        final List<Expense> expenses = expenseRepository.findAllBySettlementId(settlementId);

        final BigDecimal averageAmount = calculateAverageAmount(expenses);

        Map<Member, BigDecimal> balanceMap = calculateBalances(expenses, averageAmount);

        return calculateTransfers(balanceMap);
    }

    private BigDecimal calculateAverageAmount(List<Expense> expenses) {

        final BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalAmount.divide(new BigDecimal(expenses.size()), 2, RoundingMode.HALF_UP);
    }

    private Map<Member, BigDecimal> calculateBalances(List<Expense> expenses, BigDecimal averageAmount) {

        Map<Member, BigDecimal> balanceMap = new HashMap<>();

        expenses.forEach(expense -> {
            BigDecimal balance = expense.getAmount().subtract(averageAmount);
            balanceMap.put(expense.getMember(), balance);
        });

        return balanceMap;
    }

    private List<BalanceResDTO> calculateTransfers(Map<Member, BigDecimal> balanceMap) {

        Map<Member, BigDecimal> payers = filterPayers(balanceMap);

        Map<Member, BigDecimal> receivers = filterReceivers(balanceMap);

        return matchPayersAndReceivers(payers, receivers);
    }

    private Map<Member, BigDecimal> filterPayers(Map<Member, BigDecimal> balanceMap) {

        return balanceMap.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(BigDecimal.ZERO) < 0)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().abs()));
    }

    private Map<Member, BigDecimal> filterReceivers(Map<Member, BigDecimal> balanceMap) {

        return balanceMap.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<BalanceResDTO> matchPayersAndReceivers(Map<Member, BigDecimal> payers,
                                                        Map<Member, BigDecimal> receivers) {

        List<BalanceResDTO> results = new ArrayList<>();

        payers.forEach((payer, amountToPay) -> {
            Iterator<Map.Entry<Member, BigDecimal>> receiverIterator = receivers.entrySet().iterator();
            while (receiverIterator.hasNext() && amountToPay.compareTo(BigDecimal.ZERO) > 0) {
                Map.Entry<Member, BigDecimal> receiver = receiverIterator.next();
                BigDecimal amountCanBeReceived = receiver.getValue();
                if (amountCanBeReceived.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal transferAmount = amountToPay.min(amountCanBeReceived);
                    if (transferAmount.compareTo(BigDecimal.ZERO) > 0) {
                        results.add(new BalanceResDTO(
                                payer.getId(),
                                payer.getName(),
                                transferAmount.toString(),
                                receiver.getKey().getId(),
                                receiver.getKey().getName())
                        );

                        amountToPay = amountToPay.subtract(transferAmount);
                        receiver.setValue(amountCanBeReceived.subtract(transferAmount));
                        if (receiver.getValue().compareTo(BigDecimal.ZERO) == 0) {
                            receiverIterator.remove();
                        }
                    }
                }
            }
        });
        return results;
    }
}
