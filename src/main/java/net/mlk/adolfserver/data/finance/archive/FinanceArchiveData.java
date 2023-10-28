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
    private int finance_id;
    private double spent;
    private double remains;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}
