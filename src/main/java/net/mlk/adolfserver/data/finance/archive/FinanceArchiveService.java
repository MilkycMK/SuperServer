package net.mlk.adolfserver.data.finance.archive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FinanceArchive")
public class FinanceArchiveService {
    private static FinanceArchiveRepository financeArchiveRepository;

    @Autowired
    public FinanceArchiveService(FinanceArchiveRepository financeArchiveRepository){
        FinanceArchiveService.financeArchiveRepository = financeArchiveRepository;
    }

    public FinanceArchiveRepository getFinanceArchiveRepository() {
        return financeArchiveRepository;
    }
}
