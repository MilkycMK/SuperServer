package net.mlk.adolfserver.api.finance;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.finance.FinanceData;
import net.mlk.adolfserver.data.finance.FinanceRepository;
import net.mlk.adolfserver.data.finance.FinanceService;
import net.mlk.adolfserver.data.finance.archive.FinanceArchiveData;
import net.mlk.adolfserver.data.finance.archive.FinanceArchiveRepository;
import net.mlk.adolfserver.data.finance.archive.FinanceArchiveService;
import net.mlk.adolfserver.data.user.session.Session;
import net.mlk.adolfserver.errors.ResponseError;
import net.mlk.jmson.JsonList;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class FinanceController {

    @PostMapping(path = {"/finance", "/finance/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createFinance(@RequestParam(value = "salary") double salary,
                                                @RequestParam(value = "salary_date") String date,
                                                @RequestParam(value = "remains", required = false, defaultValue = "0") double start,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        FinanceRepository financeRepository = FinanceService.getFinanceRepository();
        Session session = (Session) request.getAttribute("session");
        int userId = session.getUserId();

        if (financeRepository.findByUserId(userId) != null) {
            return new ResponseEntity<>(new ResponseError("Учет финансов уже был создан для этого аккаунта.").toString(), HttpStatus.CONFLICT);
        } else if (salary <= 0) {
            return new ResponseEntity<>(new ResponseError("А что вы будете учитывать?").toString(), HttpStatus.BAD_REQUEST);
        } else if (!AdolfServerApplication.compareDateFormat(date)) {
            return new ResponseEntity<>(new ResponseError("Неверный формат даты.").toString(), HttpStatus.BAD_REQUEST);
        }
        LocalDate salaryDate = LocalDate.parse(date, AdolfServerApplication.DATE_FORMAT);
        FinanceData financeData = new FinanceData(userId, salary, start, salaryDate);
        return new ResponseEntity<>(JsonConverter.convertToJson(financeData).toString(), HttpStatus.OK);
    }

    @GetMapping(path = {"/finance", "/finance/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> financeGet(@RequestParam(value = "date", required = false) String date,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        FinanceRepository financeRepository = FinanceService.getFinanceRepository();
        Session session = (Session) request.getAttribute("session");
        int userId = session.getUserId();
        FinanceData financeData;

        if ((financeData = financeRepository.findByUserId(userId)) == null) {
            return new ResponseEntity<>(new ResponseError("Учет финансов не найден для этого аккаунта.").toString(), HttpStatus.BAD_REQUEST);
        }

        if (financeData.updateSalary()) {
            financeRepository.save(financeData);
        }

        if (date == null) {
            JsonList list = financeRepository.findAllDatesByUserId(userId);
            return new ResponseEntity<>(list.toString(), HttpStatus.OK);
        } else if (date.equalsIgnoreCase("now")) {
            return new ResponseEntity<>(JsonConverter.convertToJson(financeData).toString(), HttpStatus.OK);
        } else {
            if (!AdolfServerApplication.compareDateFormat(date)) {
                return new ResponseEntity<>(new ResponseError("Неверный формат даты.").toString(), HttpStatus.BAD_REQUEST);
            }
            FinanceArchiveRepository financeArchiveRepository = FinanceArchiveService.getFinanceArchiveRepository();
            FinanceArchiveData data = financeArchiveRepository
                    .findFinanceByUserIdAndMonth(userId, LocalDate.parse(date, AdolfServerApplication.DATE_FORMAT).getMonthValue());
            if (data == null) {
                return new ResponseEntity<>(new ResponseError("Отчета на эту дату не найдено.").toString(), HttpStatus.BAD_REQUEST);
            }
            financeData = new FinanceData(data);
            return new ResponseEntity<>(JsonConverter.convertToJson(financeData).toString(), HttpStatus.OK);
        }
    }

    @RequestMapping(path = "/finance/{id}", method = RequestMethod.POST, headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> financeUpdate(@PathVariable String id,
                                                @RequestParam(value = "salary", required = false, defaultValue = "-1") double salary,
                                                @RequestParam(value = "spent", required = false, defaultValue = "-1") double spent,
                                                @RequestParam(value = "description", required = false) String description,
                                                @RequestParam(value = "salary_date", required = false) String salaryDate,
                                                @RequestParam(value = "remains", required = false, defaultValue = "-1") double remains,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        FinanceArchiveRepository financeArchiveRepository = FinanceArchiveService.getFinanceArchiveRepository();
        Session session = (Session) request.getAttribute("session");
        int userId = session.getUserId();
        int finId;
        try {
            finId = Integer.parseInt(id);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseError("Айди должно быть в виде числа").toString(), HttpStatus.BAD_REQUEST);
        }
        FinanceArchiveData financeArchiveData;


        if ((financeArchiveData = financeArchiveRepository.findByIdAndUserId(finId, userId)) == null) {
            return new ResponseEntity<>(new ResponseError("Запись не найдена.").toString(), HttpStatus.BAD_REQUEST);
        } else if (remains != -1 && salary == -1 && spent == -1 && salaryDate == null && description == null) {
            return new ResponseEntity<>(JsonConverter.convertToJson(financeArchiveData).toString(), HttpStatus.OK);
        }
        if (remains != -1) {
            financeArchiveData.setRemains(remains);
        }

        if (salaryDate != null) {
            if (!AdolfServerApplication.compareDateFormat(salaryDate)) {
                return new ResponseEntity<>(new ResponseError("Неверный формат даты.").toString(), HttpStatus.BAD_REQUEST);
            }
            financeArchiveData.setSalaryDate(LocalDate.parse(salaryDate, AdolfServerApplication.DATE_FORMAT));
        }

        if (salary != -1) {
            if (salary <= 0 && salary != -1) {
                return new ResponseEntity<>(new ResponseError("Что с зарплатой?").toString(), HttpStatus.BAD_REQUEST);
            }
            financeArchiveData.setSalary(salary);
        }

        if (spent != -1){
            financeArchiveData.spent(spent);
        }

        if (description != null) {
            financeArchiveData.setDescription(description);
        }
        FinanceArchiveService.save(financeArchiveData);
        return new ResponseEntity<>(JsonConverter.convertToJson(financeArchiveData).toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/finance", method = RequestMethod.POST, headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> financeUpdate(@RequestParam(value = "salary", required = false, defaultValue = "-1") double salary,
                                                @RequestParam(value = "spent", required = false, defaultValue = "-1") double spent,
                                                @RequestParam(value = "description", required = false) String description,
                                                @RequestParam(value = "salary_date", required = false) String salaryDate,
                                                @RequestParam(value = "remains", required = false, defaultValue = "-1") double remains,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        FinanceRepository financeRepository = FinanceService.getFinanceRepository();
        Session session = (Session) request.getAttribute("session");
        int userId = session.getUserId();
        FinanceData financeData;

        if ((financeData = financeRepository.findByUserId(userId)) == null) {
            return new ResponseEntity<>(new ResponseError("Учет финансов не найден для этого аккаунта.").toString(), HttpStatus.BAD_REQUEST);
        } else if (salary == -1 && spent == -1 && salaryDate == null && remains != -1) {
            return new ResponseEntity<>(JsonConverter.convertToJson(financeData).toString(), HttpStatus.OK);
        }

        if (remains != -1) {
            financeData.setRemains(remains);
        }

        if (salaryDate != null) {
            if (!AdolfServerApplication.compareDateFormat(salaryDate)) {
                return new ResponseEntity<>(new ResponseError("Неверный формат даты.").toString(), HttpStatus.BAD_REQUEST);
            }
            financeData.setSalaryDate(LocalDate.parse(salaryDate, AdolfServerApplication.DATE_FORMAT));
        }

        if (salary != -1) {
            if (salary <= 0 && salary != -1) {
                return new ResponseEntity<>(new ResponseError("Что с зарплатой?").toString(), HttpStatus.BAD_REQUEST);
            }
            financeData.setSalary(salary);
        }
        if (spent != -1) {
            financeData.spent(spent, description);
        }

        financeData.updateSalary();
        FinanceService.save(financeData);
        return new ResponseEntity<>(JsonConverter.convertToJson(financeData).toString(), HttpStatus.OK);
    }

    @DeleteMapping(path = {"/finance", "/finance"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> financeDelete(@RequestParam(value = "date", required = false) String date,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        FinanceRepository financeRepository = FinanceService.getFinanceRepository();
        FinanceArchiveRepository financeArchiveRepository = FinanceArchiveService.getFinanceArchiveRepository();
        Session session = (Session) request.getAttribute("session");
        int userId = session.getUserId();
        FinanceData financeData;

        if (date != null) {
            if (!AdolfServerApplication.compareDateFormat(date)) {
                return new ResponseEntity<>(new ResponseError("Неверный формат даты.").toString(), HttpStatus.BAD_REQUEST);
            }

            financeData = financeRepository.findByUserIdAndMonth(userId,
                    LocalDate.parse(date, AdolfServerApplication.DATE_FORMAT).getMonthValue());
            if (financeData == null) {
                return new ResponseEntity<>(new ResponseError("Отчета на эту дату не найдено.").toString(), HttpStatus.BAD_REQUEST);
            }
            financeRepository.delete(financeData);
            financeArchiveRepository.deleteAll(financeData.getArchiveData());
            return new ResponseEntity<>(JsonConverter.convertToJson(financeData).toString(), HttpStatus.OK);
        }

        if ((financeData = financeRepository.findByUserId(userId)) == null) {
            return new ResponseEntity<>(new ResponseError("Учет финансов не найден для этого аккаунта.").toString(), HttpStatus.BAD_REQUEST);
        } else {
            financeRepository.delete(financeData);
            financeArchiveRepository.deleteAll(financeData.getArchiveData());
            return new ResponseEntity<>(JsonConverter.convertToJson(financeData).toString(), HttpStatus.OK);
        }
    }

}
