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
    private double remains;
    @Column(name = "creation_date")
    @JsonField(key = "creation_date")
    private LocalDate creationDate;
    @Column(name = "salary_date")
    @JsonField(key = "salary_date")
    private LocalDate salaryDate;

    protected Finance() {

    }

    public Finance(int userId, double salary, double remains, LocalDate salaryDate) {
        this.userId = userId;
        this.salary = salary;
        this.creationDate = LocalDate.now();
        this.remains += remains;
        this.salaryDate = salaryDate;
        boolean updated = this.updateSalary();
        FinanceService.save(this);
        if (updated) {
            Transaction transaction = new Transaction(this.id, "add", this.salary, 0, this.salary, this.remains,
                    "the salary", LocalDate.now(), this.salaryDate);
        }
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setRemains(double remains) {
        this.remains = remains;
    }

    public void setSalaryDate(LocalDate salaryDate) {
        this.salaryDate = salaryDate;
    }

    public Transaction spend(double spend, String description) {
        this.remains -= spend < 0 ?  spend * -1 : spend;
        Transaction transaction = new Transaction(this.id, "spend", this.salary, spend, 0, this.remains,
                description, LocalDate.now(), this.salaryDate);
        TransactionService.save(transaction);
        return transaction;
    }

    public Transaction add(double add, String description) {
        this.remains += add < 0 ?  add * -1 : add;
        Transaction transaction = new Transaction(this.id, "add", this.salary, 0, add, this.remains,
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
                this.remains += this.salary;
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
        return this.remains;
    }

    public LocalDate getCreationTime() {
        return this.creationDate;
    }
}
