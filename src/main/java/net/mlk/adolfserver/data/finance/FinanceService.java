package net.mlk.adolfserver.data.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FinanceService")
public class FinanceService {
    private static FinanceRepository financeRepository;

    @Autowired
    public FinanceService(FinanceRepository financeRepository) {
        FinanceService.financeRepository = financeRepository;
    }

    public static void save(FinanceData financeData) {
        financeRepository.save(financeData);
    }

    public static FinanceRepository getFinanceRepository() {
        return financeRepository;
    }
}
