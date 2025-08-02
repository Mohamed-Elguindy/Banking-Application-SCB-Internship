package com.elguindy.bankingapplication.repository;

import com.elguindy.bankingapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);
}
