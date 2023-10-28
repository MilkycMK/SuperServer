package net.mlk.adolfserver.data.finance;

import net.mlk.adolfserver.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRepository extends JpaRepository<User, Integer> {
//    FinanceData findBy
}
