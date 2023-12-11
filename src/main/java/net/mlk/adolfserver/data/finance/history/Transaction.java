package net.mlk.adolfserver.data.finance.history;

import jakarta.persistence.*;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.annotations.JsonIgnore;
import net.mlk.jmson.utils.JsonConvertible;

import java.time.LocalDate;

@Table(name = "transactions")
@Entity
public class Transaction implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "finance_id")
    @JsonIgnore
    private int financeId;
    private String type;
    private double salary;
    private double value;
    private double remains;
    private String description;
    @Column(name = "creation_date")
    @JsonField(key = "creation_date", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDate creationDate;
    @Column(name = "salary_date")
    @JsonField(key = "salary_date", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDate salaryDate;

    protected Transaction() {

    }

    public Transaction(int financeId, String type, double salary, double spent, double added, double remains, String description,
                       LocalDate creationDate, LocalDate salaryDate) {
        this.financeId = financeId;
        this.salary = salary;
        this.type = type;
        if (this.type.equalsIgnoreCase("add")) {
            this.value = added;
        } else {
            this.value = spent;
        }
        this.remains = remains;
        this.description = description;
        this.creationDate = creationDate;
        this.salaryDate = salaryDate;
        TransactionService.save(this);
    }

    public void setRemains(double remains) {
        this.remains = remains;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setSalaryDate(LocalDate date) {
        this.salaryDate = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type, double value) {
        this.type = type;
        if (type.equalsIgnoreCase("add")) {
            this.remains += value;
        } else {
            this.remains -= value;
        }
    }

    public int getId() {
        return this.id;
    }

    public int getFinanceId() {
        return this.financeId;
    }

    public double getSalary() {
        return this.salary;
    }

    public double getValue() {
        return this.value;
    }

    public double getRemains() {
        return this.remains;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }
}
