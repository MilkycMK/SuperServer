package net.mlk.adolfserver.data.finance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRepository extends JpaRepository<FinanceData, Integer> {
    FinanceData findByUserId(int userId);
}
