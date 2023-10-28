package net.mlk.adolfserver.data.finance.archive;

import jakarta.persistence.*;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.utils.JsonConvertible;

import java.time.LocalDate;

@Table(name = "finance_archive")
@Entity
public class FinanceArchiveData implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    @JsonField(key = "user_id")
    private int userId;
    private double salary;
    private double spent;
    private double remains;
    private String description;
    @Column(name = "creation_date")
    @JsonField(key = "creation_date")
    private LocalDate creationDate;
    @Column(name = "salary_date")
    @JsonField(key = "salary_date")
    private LocalDate salaryDate;

    protected FinanceArchiveData() {

    }

    public FinanceArchiveData(int userId, double salary, double spent, double remains, String description,LocalDate creationDate, LocalDate salaryDate) {
        this.userId = userId;
        this.salary = salary;
        this.spent = spent;
        this.remains = remains;
        this.description = description;
        this.creationDate = creationDate;
        this.salaryDate = salaryDate;
        FinanceArchiveService.save(this);
    }

    public void setSalaryDate(LocalDate salaryDate) {
        this.salaryDate = salaryDate;
    }

    public void setRemains(double remains) {
        this.remains = remains;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getSalaryDate() {
        return this.salaryDate;
    }

    public void spent(double spent) {
        if (this.spent < spent) {
            this.remains -= spent - this.spent;
        } else {
            this.remains += this.spent == spent ? 0 : this.spent - spent;
        }
        this.spent = spent;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getSpent() {
        return this.spent;
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
