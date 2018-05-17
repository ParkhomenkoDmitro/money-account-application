package com.parkhomenko;

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
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
public class Organization {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    
    @Version
    @Column(name="OPTLOCK")
    public Integer version;
    
    @Basic
    public String name;
    
    @Basic
    public BigDecimal amount;

    public Organization(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
    }
}
