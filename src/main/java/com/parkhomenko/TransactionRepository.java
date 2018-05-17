package com.parkhomenko;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author dmytro
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
}
