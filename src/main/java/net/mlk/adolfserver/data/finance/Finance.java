package net.mlk.adolfserver.data.finance;

import jakarta.persistence.*;
import net.mlk.adolfserver.data.finance.history.Transaction;
import net.mlk.adolfserver.data.finance.history.TransactionService;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.annotations.JsonIgnore;
import net.mlk.jmson.utils.JsonConvertible;

import java.time.LocalDate;

@Table(name = "finance")
@Entity
public class Finance implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    @JsonIgnore
    private int userId;
    private double salary;
    private double remain;
    @Column(name = "creation_date")
    @JsonField(key = "creation_date", dateFormat = "yyyy-MM-dd")
    private LocalDate creationDate;
    @Column(name = "salary_date")
    @JsonField(key = "salary_date", dateFormat = "yyyy-MM-dd")
    private LocalDate salaryDate;

    protected Finance() {

    }

    public Finance(int userId, double salary, double remains, LocalDate salaryDate) {
        this.userId = userId;
        this.salary = salary;
        this.creationDate = LocalDate.now();
        this.remain += remains;
        this.salaryDate = salaryDate;
        boolean updated = this.updateSalary();
        FinanceService.save(this);
        if (updated) {
            Transaction transaction = new Transaction(this.id, "add", this.salary, 0, this.salary, this.remain,
                    "the salary", LocalDate.now(), this.salaryDate);
        }
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setRemains(double remains) {
        this.remain = remains;
    }

    public void setSalaryDate(LocalDate salaryDate) {
        this.salaryDate = salaryDate;
    }

    public Transaction spend(double spend, String description) {
        this.remain -= spend < 0 ?  spend * -1 : spend;
        Transaction transaction = new Transaction(this.id, "spend", this.salary, spend, 0, this.remain,
                description, LocalDate.now(), this.salaryDate);
        TransactionService.save(transaction);
        return transaction;
    }

    public Transaction add(double add, String description) {
        this.remain += add < 0 ?  add * -1 : add;
        Transaction transaction = new Transaction(this.id, "add", this.salary, 0, add, this.remain,
                description, LocalDate.now(), this.salaryDate);
        TransactionService.save(transaction);
        return transaction;
    }

    public boolean updateSalary() {
        LocalDate today = LocalDate.now();
        if (today.isAfter(this.salaryDate) || this.salaryDate.isEqual(today)) {
            if (this.id != 0) {
                this.add(this.salary, "the salary");
            } else {
                this.remain += this.salary;
            }
            this.salaryDate = today.plusMonths(1);
            return true;
        }
        return false;
    }

    public LocalDate getSalaryDate() {
        return this.salaryDate;
    }

    public int getId() {
        return this.id;
    }

    public int getUserId() {
        return this.userId;
    }

    public double getSalary() {
        return this.salary;
    }

    public double getRemains() {
        return this.remain;
    }

    public LocalDate getCreationTime() {
        return this.creationDate;
    }
}
