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
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @OneToMany
    @JoinColumns({
            @JoinColumn(updatable=false,insertable=false, name="finance_id", referencedColumnName="id"),
            @JoinColumn(updatable=false,insertable=false, name="user_name", referencedColumnName="user_id"),
    })
    private List<FinanceArchiveData> archiveData;

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

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }
}
