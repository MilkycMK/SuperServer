package net.mlk.adolfserver.data.finance;

import net.mlk.adolfserver.data.finance.archive.FinanceArchiveData;
import net.mlk.jmson.JsonList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinanceRepository extends JpaRepository<FinanceData, Integer> {
    FinanceData findByUserId(int userId);
    @Query(value = "SELECT * FROM finance_archive WHERE user_id=?1 AND EXTRACT(MONTH FROM creation_date)=?2", nativeQuery = true)
    FinanceData findByUserIdAndMonth(int userId, int month);
    @Query(value = "SELECT DISTINCT creation_date FROM finance_archive WHERE user_id=?1", nativeQuery = true)
    JsonList findAllDatesByUserId(int id);
}
