package net.mlk.adolfserver.data.finance;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "finance")
@Entity
public class FinanceData {
    @Id
    private int id;
    private String userName;
}
