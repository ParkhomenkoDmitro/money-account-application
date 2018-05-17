package com.parkhomenko;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author dmytro
 */

@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    
    @Basic
    @Column(name = "history_date")
    public LocalDateTime date;
    
    @Basic
    public Boolean isCredit;
    
    @ManyToOne(fetch = FetchType.LAZY)
    public Organization organization;
    
    @Basic
    public BigDecimal amount;

    public Transaction(LocalDateTime date, Boolean isCredit, Organization organization, BigDecimal amount) {
        this.date = date;
        this.isCredit = isCredit;
        this.organization = organization;
        this.amount = amount;
    }
}
