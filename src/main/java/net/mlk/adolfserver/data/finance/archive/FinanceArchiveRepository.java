package net.mlk.adolfserver.data.finance.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceArchiveRepository extends JpaRepository<FinanceArchiveData, Integer> {
    @Query(value = "SELECT * FROM finance_archive WHERE spent=0 AND user_id=?1 AND EXTRACT(MONTH FROM creation_date)=?2", nativeQuery = true)
    FinanceArchiveData findFinanceByUserIdAndMonth(int userId, int month);
    @Query(value = "SELECT * FROM finance_archive WHERE spent!=0 AND user_id=?1 AND EXTRACT(MONTH FROM creation_date)=?2", nativeQuery = true)
    List<FinanceArchiveData> findByUserIdAndMonth(int userId, int month);
    FinanceArchiveData findByIdAndUserId(int id, int userId);
}
