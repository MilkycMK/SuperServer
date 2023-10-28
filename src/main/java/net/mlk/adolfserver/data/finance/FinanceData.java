package net.mlk.adolfserver.data.finance;

import jakarta.persistence.*;
import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.finance.archive.FinanceArchiveData;
import net.mlk.adolfserver.data.finance.archive.FinanceArchiveService;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.utils.JsonConvertible;

import java.time.LocalDate;
import java.util.List;

@Table(name = "finance")
@Entity
public class FinanceData implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    @JsonField(key = "user_id")
    private int userId;
    private double salary;
    private double remains;
    @Column(name = "creation_date")
    @JsonField(key = "creation_date")
    private LocalDate creationDate;
    @Column(name = "salary_date")
    @JsonField(key = "salary_date")
    private LocalDate salaryDate;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(updatable=false, insertable=false, name="user_id", referencedColumnName="user_id"),
            @JoinColumn(updatable = false, insertable = false, name = "creation_date", referencedColumnName = "creation_date")
    })
    private List<FinanceArchiveData> archiveData;

    protected FinanceData() {

    }

    public FinanceData(int userId, double salary, double remains, LocalDate salaryDate) {
        this.userId = userId;
        this.salary = salary;
        this.remains = salary;
        this.creationDate = LocalDate.now();
        this.remains += remains;
        this.salaryDate = salaryDate;
        FinanceService.save(this);
        FinanceArchiveData financeArchiveData = new FinanceArchiveData(userId, salary, 0, this.remains, null, this.creationDate, this.salaryDate);
    }

    public FinanceData(FinanceArchiveData financeArchiveData) {
        this.userId = financeArchiveData.getUserId();
        this.salary = financeArchiveData.getSalary();
        this.remains = financeArchiveData.getRemains();
        this.creationDate = financeArchiveData.getCreationDate();
        this.archiveData = FinanceArchiveService.getFinanceArchiveRepository()
                .findByUserIdAndMonth(this.userId, this.creationDate.getMonthValue());
        this.salaryDate = financeArchiveData.getSalaryDate();
    }

    public List<FinanceArchiveData> getArchiveData() {
        return this.archiveData;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setSalaryDate(LocalDate salaryDate) {
        this.salaryDate = salaryDate;
    }

    public void setRemains(double remains) {
        this.remains = remains;
    }

    public void spent(double toSpent, String description) {
        this.remains -= toSpent < 0 ?  toSpent * -1 : toSpent;
        FinanceArchiveData financeArchiveData = new FinanceArchiveData(this.userId, this.salary, toSpent, this.remains, description, LocalDate.now(), this.salaryDate);
        FinanceArchiveService.save(financeArchiveData);
        this.archiveData.add(financeArchiveData);
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
