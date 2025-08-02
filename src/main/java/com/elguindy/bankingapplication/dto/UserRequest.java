package com.elguindy.bankingapplication.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String otherName;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
}
