package com.parkhomenko;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author dmytro
 */

@ToString
@EqualsAndHashCode
public class OrganizationDto {
    
    public final Long id;
    public final String name;
    public final BigDecimal amount;
    
    public OrganizationDto() {
        id = 0L;
        name = "";
        amount = BigDecimal.ZERO;
    }

    public OrganizationDto(Long id, String name, BigDecimal amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }
}
