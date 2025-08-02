package com.elguindy.bankingapplication.service;

import com.elguindy.bankingapplication.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailwithAttachment(EmailDetails emailDetails);

}
