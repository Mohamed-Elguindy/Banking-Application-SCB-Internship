package com.elguindy.bankingapplication.service;

import com.elguindy.bankingapplication.dto.EmailDetails;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailServiceImp implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;


    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMessageBody());
            javaMailSender.send(mailMessage);
            System.out.println("Email Sent");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailwithAttachment(EmailDetails emailDetails) {
        MimeMessage  mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper ;
        try{
            mimeMessageHelper =new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            mimeMessageHelper.setText(emailDetails.getMessageBody());
            FileSystemResource file = new FileSystemResource(emailDetails.getAttachment());
            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()),file);
            javaMailSender.send(mimeMessage);

            System.out.println("Email Sent");


        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
