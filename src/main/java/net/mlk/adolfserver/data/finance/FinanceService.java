package net.mlk.adolfserver.data.finance;

import net.mlk.adolfserver.data.todo.TodoElement;
import net.mlk.jmson.Json;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("FinanceService")
public class FinanceService {
    private static FinanceRepository financeRepository;

    @Autowired
    public FinanceService(FinanceRepository financeRepository) {
        FinanceService.financeRepository = financeRepository;
    }

    public static Finance findByUserIdAndMonthAndYear(int userId, int month, int year) {
        return financeRepository.findByUserIdAndMonthAndYear(userId, month, year);
    }

    public static Finance findByUserId(int userId) {
        return financeRepository.findTopByUserIdOrderByUserIdDesc(userId);
    }

    public static List<Finance> findAllByUserId(int userId) {
        return financeRepository.findAllByUserId(userId);
    }

    public static List<Json> findAllHistoryJsonsByUserId(int userId) {
        List<Finance> todo = findAllByUserId(userId);
        todo.remove(todo.size() - 1);
        return todo.stream()
                .map(JsonConverter::convertToJson)
                .collect(Collectors.toList());
    }

    public static Finance findByIdAndUserId(int id, int userId) {
        return financeRepository.findByIdAndUserId(id, userId);
    }

    public static void save(Finance finance) {
        financeRepository.save(finance);
        financeRepository.flush();
    }

    public static void delete(Finance finance) {
        financeRepository.delete(finance);
    }

    public static void deleteAll(List<Finance> finances) {
        financeRepository.deleteAll(finances);
    }

}
