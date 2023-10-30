package net.mlk.adolfserver.data.finance.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query(value = "SELECT * FROM transactions WHERE finance_id=?1 " +
            "AND EXTRACT(MONTH FROM creation_date)=?2", nativeQuery = true)
    List<Transaction> findAllByFinanceIdAndMonth(int financeId, int month);
    List<Transaction> findAllByFinanceId(int financeId);
    Transaction findByIdAndFinanceId(int id, int financeId);
}
