package net.mlk.adolfserver.data.finance.history;

import net.mlk.jmson.Json;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("FinanceHistoryService")
public class TransactionService {
    private static TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        TransactionService.transactionRepository = transactionRepository;
    }

    public static List<Transaction> findAllByUserIdAndMonth(int financeId, int month) {
        return transactionRepository.findAllByFinanceIdAndMonth(financeId, month);
    }

    public static List<Transaction> findAllByFinanceId(int financeId) {
        return transactionRepository.findAllByFinanceId(financeId);
    }

    public static Transaction findByIdAndFinanceId(int id, int financeId) {
        return transactionRepository.findByIdAndFinanceId(id, financeId);
    }

    public static List<Json> findAllJsonsByFinanceId(int financeId) {
        List<Transaction> todo = findAllByFinanceId(financeId);
        return todo.stream()
                .map(JsonConverter::convertToJson)
                .collect(Collectors.toList());
    }

    public static void save(Transaction transaction) {
        transactionRepository.save(transaction);
        transactionRepository.flush();
    }

    public static void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }
}
