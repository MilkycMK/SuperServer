package net.mlk.adolfserver.data.finance.archive;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Table(name = "finance_archive")
@Entity
public class FinanceArchiveData {
    @Id
    private int id;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "finance_id")
    private int financeId;
    private double spent;
    private double remains;
    private String description;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    public int getId() {
        return this.id;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getFinanceId() {
        return this.financeId;
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

    public LocalDateTime getCreationDate() {
        return this.creationTime;
    }
}
