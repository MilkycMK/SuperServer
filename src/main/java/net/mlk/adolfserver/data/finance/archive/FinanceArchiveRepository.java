package net.mlk.adolfserver.data.finance.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceArchiveRepository extends JpaRepository<FinanceArchiveData, Integer> {
}
