package com.elguindy.bankingapplication.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor

public class ErrorResponse {

    private String code;

    private String message;

    private String errorID;

    public ErrorResponse(String code, String message){
        this.code = code;
        this.message = message;
        errorID = UUID.randomUUID().toString().replaceAll("\\D", "").substring(0, 10);
    }



}
