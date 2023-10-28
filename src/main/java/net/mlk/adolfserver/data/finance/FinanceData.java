package net.mlk.adolfserver.data.finance;

import jakarta.persistence.*;
import net.mlk.adolfserver.data.finance.archive.FinanceArchiveData;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "finance")
@Entity
public class FinanceData {
    @Id
    private int id;
    @Column(name = "user_id")
    private int userId;
    private double salary;
    private double remains;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
    @OneToMany
    @JoinColumns({
            @JoinColumn(updatable=false,insertable=false, name="finance_id", referencedColumnName="id"),
            @JoinColumn(updatable=false,insertable=false, name="user_id", referencedColumnName="user_id"),
    })
    private List<FinanceArchiveData> archiveData;

    protected  FinanceData() {

    }

    public FinanceData(int userId, double salary) {
        this.userId = userId;
        this.salary = salary;
        this.remains = salary;
        this.creationTime = LocalDateTime.now();
        FinanceService.save(this);
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

    public LocalDateTime getCreationTime() {
        return this.creationTime;
    }
}
