package net.mlk.adolfserver.data.finance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Integer> {
    @Query(value = "SELECT * FROM finance WHERE user_id=?1 AND EXTRACT(MONTH FROM creation_date)=?2 " +
            "AND EXTRACT(YEAR FROM creation_date)=?3", nativeQuery = true)
    Finance findByUserIdAndMonthAndYear(int userId, int month, int year);
    Finance findTopByUserIdOrderByUserIdDesc(int userId);
    Finance findByIdAndUserId(int id, int userId);
    List<Finance> findAllByUserId(int userId);

}
