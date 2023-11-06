package net.mlk.adolfserver.api.finance;

import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.finance.Finance;
import net.mlk.adolfserver.data.finance.FinanceService;
import net.mlk.adolfserver.data.finance.history.Transaction;
import net.mlk.adolfserver.data.finance.history.TransactionService;
import net.mlk.adolfserver.data.user.session.Session;
import net.mlk.adolfserver.errors.ResponseError;
import net.mlk.adolfserver.utils.AdolfUtils;
import net.mlk.jmson.Json;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class FinanceController {

    @PostMapping(path = {"/api/finances", "/api/finances/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> createFinance(@RequestParam double salary,
                                                       @RequestParam(name = "salary_date") String date,
                                                       @RequestParam(required = false, defaultValue = "0") double remain,
                                                       @RequestAttribute Session session) {
        int userId = session.getUserId();

        if (FinanceService.findByUserId(userId) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (salary <= 0) {
            return new ResponseEntity<>(new ResponseError("Very strange salary..."), HttpStatus.BAD_REQUEST);
        } else if (!AdolfUtils.compareDateFormat(date)) {
            return new ResponseEntity<>(new ResponseError("Wrong date format."), HttpStatus.BAD_REQUEST);
        }

        LocalDate salaryDate = LocalDate.parse(date, AdolfServerApplication.DATE_FORMAT);
        Finance finance = new Finance(userId, salary, remain, salaryDate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/finances");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(path = {"/api/finances/transactions", "/api/finances/transactions/"})
    public ResponseEntity<ResponseError> spendMoney(@RequestParam String type,
                                                    @RequestParam double value,
                                                    @RequestParam(required = false) String description,
                                                    @RequestAttribute Session session) {
        int userId = session.getUserId();
        Finance finance;

        if ((finance = validateCurrentDateFinance(userId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!type.isEmpty() && !type.equalsIgnoreCase("spend") && !type.equalsIgnoreCase("add")) {
            return new ResponseEntity<>(new ResponseError("Unknown type. Allowed: [spend, add]"), HttpStatus.BAD_REQUEST);
        }

        Transaction transaction;
        if (type.equalsIgnoreCase("spend")) {
             transaction = finance.spend(value, description);
        } else {
            transaction = finance.add(value, description);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/finances/transactions/" + transaction.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(path = {"/api/finances/transactions", "/api/finances/transactions/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Json>> getTransactions(@RequestAttribute Session session) {
        int userId = session.getUserId();
        Finance finance;

        if ((finance = validateCurrentDateFinance(userId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(TransactionService.findAllJsonsByFinanceId(finance.getId()), HttpStatus.OK);
    }

    @GetMapping(path = {"/api/finances", "/api/finances/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Json> getFinance(@RequestAttribute Session session) {
        int userId = session.getUserId();
        Finance finance;

        if ((finance = validateCurrentDateFinance(userId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (finance.updateSalary()) {
            FinanceService.save(finance);
        }
        return new ResponseEntity<>(JsonConverter.convertToJson(finance), HttpStatus.OK);
    }

    @GetMapping(path = {"/api/finances/history", "/api/finances/history/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Json>> getFinancesHistory(@RequestAttribute Session session) {
        int userId = session.getUserId();
        return new ResponseEntity<>(FinanceService.findAllHistoryJsonsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping(path = {"/api/finances/history/{fId}/transactions", "/api/finances/history/{fId}/transactions/"})
    public ResponseEntity<List<Json>> getHistoryTransactions(@PathVariable String fId,
                                                             @RequestAttribute Session session) {
        int userId = session.getUserId();
        int financeId = AdolfUtils.tryParseInteger(fId);

        if (FinanceService.findByIdAndUserId(financeId, userId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(TransactionService.findAllJsonsByFinanceId(financeId), HttpStatus.OK);
    }

    @PatchMapping(path = {"/api/finances", "/api/finances/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(path = {"/api/finances", "/api/finances/"},
            method = RequestMethod.POST,
            headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> patchFinances(@RequestParam double salary,
                                                       @RequestParam double remains,
                                                       @RequestParam(value = "salary_date") String date,
                                                       @RequestAttribute Session session) {
        int userId = session.getUserId();
        Finance finance;

        if ((finance = validateCurrentDateFinance(userId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return this.updateFinance(true, finance, salary, remains, date);
    }

    @PatchMapping(path = {"/api/finances/history/{fId}", "/api/finances/history/{fId}/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(path = {"/api/finances/history/{fId}", "/api/finances/history/{fId}/"},
            method = RequestMethod.POST,
            headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> patchFinancesHistory(@PathVariable String fId,
                                                              @RequestParam double salary,
                                                              @RequestParam double remains,
                                                              @RequestParam(value = "salary_date") String date,
                                                              @RequestAttribute Session session) {
        int userId = session.getUserId();
        int financeId = AdolfUtils.tryParseInteger(fId);
        Finance finance;

        if ((finance = FinanceService.findByIdAndUserId(financeId, userId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return this.updateFinance(false, finance, salary, remains, date);
    }

    @PatchMapping(path = {"/api/finances/history/{fId}/transactions/{tId}", "/api/finances/history/{fId}/transactions/{tId}/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(path = {"/api/finances/history/{fId}/transactions/{tId}", "/api/finances/history/{fId}/transactions/{tId}/"},
            method = RequestMethod.POST,
            headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> patchTransactionHistory(@PathVariable String fId,
                                                                 @PathVariable String tId,
                                                                 @RequestParam String type,
                                                                 @RequestParam double value,
                                                                 @RequestParam double salary,
                                                                 @RequestParam double remains,
                                                                 @RequestParam String description,
                                                                 @RequestParam(value = "salary_date") String date,
                                                                 @RequestAttribute Session session) {
        int userId = session.getUserId();
        int financeId = AdolfUtils.tryParseInteger(fId);
        int transactionId = AdolfUtils.tryParseInteger(tId);
        Transaction transaction;

        if (FinanceService.findByIdAndUserId(financeId, userId) == null ||
                (transaction = TransactionService.findByIdAndFinanceId(transactionId, financeId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return updateTransaction(transaction, type, value, salary, remains, description, date);
    }

    @PatchMapping(path = {"/api/finances/transactions/{tId}", "/api/finances/transactions/{tId}/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(path = {"/api/finances/transactions/{tId}", "/api/finances/transactions/{tId}/"},
            method = RequestMethod.POST,
            headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> patchTransaction(@PathVariable String tId,
                                                          @RequestParam String type,
                                                          @RequestParam double value,
                                                          @RequestParam double salary,
                                                          @RequestParam double remains,
                                                          @RequestParam String description,
                                                          @RequestParam(value = "salary_date") String date,
                                                          @RequestAttribute Session session) {
        int userId = session.getUserId();
        int transactionId = AdolfUtils.tryParseInteger(tId);
        Finance finance;
        Transaction transaction;

        if ((finance = validateCurrentDateFinance(userId)) == null ||
                (transaction = TransactionService.findByIdAndFinanceId(transactionId, finance.getId())) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return updateTransaction(transaction, type, value, salary, remains, description, date);
    }


    @DeleteMapping(path = {"/api/finances/history/{fId}", "/api/finances/history/{fId}/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> deleteHistoryFinance(@PathVariable String fId,
                                                              @RequestAttribute Session session) {
        int userId = session.getUserId();
        int financeId = AdolfUtils.tryParseInteger(fId);
        Finance finance;

        if ((finance = FinanceService.findByIdAndUserId(financeId, userId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FinanceService.delete(finance);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/api/finances/history/{fId}/transactions/{tId}", "/api/finances/history/{fId}/transactions/{tId}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> deleteHistoryTransaction(@PathVariable String fId,
                                                                  @PathVariable String tId,
                                                                  @RequestAttribute Session session) {
        int userId = session.getUserId();
        int financeId = AdolfUtils.tryParseInteger(fId);
        int transactionId = AdolfUtils.tryParseInteger(tId);
        Transaction transaction;

        if (FinanceService.findByIdAndUserId(financeId, userId) == null ||
            (transaction = TransactionService.findByIdAndFinanceId(transactionId, financeId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TransactionService.delete(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/api/finances/transactions/{tId}", "/api/finances/transactions/{tId}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> deleteTransaction(@PathVariable String tId,
                                                           @RequestAttribute Session session) {
        int userId = session.getUserId();
        int transactionId = AdolfUtils.tryParseInteger(tId);
        Finance finance;
        Transaction transaction;

        if ((finance = validateCurrentDateFinance(userId)) == null ||
            (transaction = TransactionService.findByIdAndFinanceId(transactionId, finance.getId())) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TransactionService.delete(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/api/finances", "/api/finances/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> deleteFinance(@RequestAttribute Session session) {
        int userId = session.getUserId();
        List<Finance> finances;

        if ((finances = FinanceService.findAllByUserId(userId)).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FinanceService.deleteAll(finances);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static Finance validateCurrentDateFinance(int userId) {
        Finance finance;
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();
        if ((finance = FinanceService.findByUserIdAndMonthAndYear(userId, month, year)) == null) {
            if ((finance = FinanceService.findByUserId(userId)) == null) {
                return null;
            }
            if (finance.getCreationTime().getMonthValue() != LocalDate.now().getMonthValue()) {
                finance = new Finance(userId, finance.getSalary(), finance.getRemains(), finance.getSalaryDate());
                finance.updateSalary();
            }
        }
        return finance;
    }

    private ResponseEntity<ResponseError> updateTransaction(Transaction transaction, String type,
                                                            double value, double salary, double remains,
                                                            String description, String date) {
        if (salary <= 0) {
            return new ResponseEntity<>(new ResponseError("Very strange salary..."), HttpStatus.BAD_REQUEST);
        } else if (!AdolfUtils.compareDateFormat(date)) {
            return new ResponseEntity<>(new ResponseError("Wrong date format."), HttpStatus.BAD_REQUEST);
        }

        LocalDate salaryDate = LocalDate.parse(date, AdolfServerApplication.DATE_FORMAT);
        transaction.setDescription(description);
        transaction.setRemains(remains);
        transaction.setType(type, value);
        transaction.setSalary(salary);
        transaction.setSalaryDate(salaryDate);
        TransactionService.save(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<ResponseError> updateFinance(boolean updateSalary, Finance finance,
                                                        @RequestParam double salary, @RequestParam double remains,
                                                        @RequestParam(value = "salary_date") String date) {
        if (salary <= 0) {
            return new ResponseEntity<>(new ResponseError("Very strange salary..."), HttpStatus.BAD_REQUEST);
        } else if (!AdolfUtils.compareDateFormat(date)) {
            return new ResponseEntity<>(new ResponseError("Wrong date format."), HttpStatus.BAD_REQUEST);
        }

        LocalDate salaryDate = LocalDate.parse(date, AdolfServerApplication.DATE_FORMAT);
        finance.setRemains(remains);
        finance.setSalary(salary);
        finance.setSalaryDate(salaryDate);
        if (updateSalary) {
            finance.updateSalary();
        }
        FinanceService.save(finance);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
