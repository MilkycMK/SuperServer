package net.mlk.adolfserver.api.finance;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.mlk.adolfserver.data.finance.FinanceData;
import net.mlk.adolfserver.data.finance.FinanceRepository;
import net.mlk.adolfserver.data.finance.FinanceService;
import net.mlk.adolfserver.data.user.session.Session;
import net.mlk.adolfserver.errors.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FinanceController {

    @PostMapping(path = {"/finance", "/finance/"})
    public ResponseEntity<String> createFinanceTable(@RequestParam(value = "salary") double salary,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        FinanceRepository financeRepository = FinanceService.getFinanceRepository();
        Session session = (Session) request.getSession();
        int userId = session.getUserId();

        if (financeRepository.findByUserId(userId) != null) {
            return new ResponseEntity<>(new ResponseError("Учет финансов уже был создан для этого аккаунта.").toString(), HttpStatus.CONFLICT);
        }
        else if (salary <= 0) {
            return new ResponseEntity<>(new ResponseError("Странный вы человек, если у вас зарплата меньше 1...").toString(), HttpStatus.BAD_REQUEST);
        }

        FinanceData financeData = new FinanceData(userId, salary);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
