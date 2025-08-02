package com.elguindy.bankingapplication.service;

import com.elguindy.bankingapplication.dto.*;
import com.elguindy.bankingapplication.entity.User;
import com.elguindy.bankingapplication.repository.UserRepository;
import com.elguindy.bankingapplication.util.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class UserServiceImpl implements UserService {
    @Autowired UserRepository userRepository;
    @Autowired EmailService emailService;
    @Autowired TransactionService transactionService;



    @Override
    public BankResponse createUser(UserRequest userRequest) {
        if(! (userRepository.existsByEmail(userRequest.getEmail()) )) {
            User newUser = User.builder().
                    firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .otherName(userRequest.getOtherName())
                    .email(userRequest.getEmail())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .accountNumber(AccountUtil.genrateAccountNumber())
                    .accountBalance(BigDecimal.ZERO)
                    .status("Active")
                    .build();
            User savedUser =userRepository.save(newUser);
            BankResponse bankResponse = BankResponse.builder()
                    .ResponseMessage(AccountUtil.Account_Created_Successfully_Message).accountInfo(AccountInfo.builder().
                            accountBalance(savedUser.getAccountBalance()).
                            accountNumber(savedUser.getAccountNumber()).
                            accountName(savedUser.getFirstName()+" "+savedUser.getLastName()).build())
                    .ResponseCode(AccountUtil.Account_Created_Successfully_Code).build();

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(savedUser.getEmail())
                    .subject("Account Created")
                    .messageBody("Congratulations! Your Account has been Created \nYour Account Details : " +
                            "Account Name : "+savedUser.getFirstName()+" "+savedUser.getLastName()+"\n" +
                            "Account Number: "+ savedUser.getAccountNumber()).build();

            emailService.sendEmailAlert(emailDetails);





            return bankResponse;

        }
        else{
            BankResponse bankResponse = BankResponse.builder()
                    .ResponseMessage(AccountUtil.Account_Exists_Message).accountInfo(null)
                    .ResponseCode(AccountUtil.Account_Exists_Code).build();
            return bankResponse;
        }

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        if(userRepository.existsByAccountNumber(request.getAccountNumber())){
            User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
            return BankResponse.builder()
                    .ResponseCode(AccountUtil.Account_Found_Code)
                    .ResponseMessage(AccountUtil.Account_Found_Message)
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(foundUser.getAccountBalance())
                            .accountName(foundUser.getFirstName()+" " +foundUser.getLastName())
                            .accountNumber(foundUser.getAccountNumber()).build()).build();
        }
        else{
            return BankResponse.builder()
                    .ResponseCode(AccountUtil.Account_Doesnt_Exists_Code)
                    .ResponseMessage(AccountUtil.Account_Doesnt_Exists_Message)
                    .accountInfo(null).build();
        }
    }

    @Override
    public BankResponse deposit(DepositRequest depositRequest) {
        if(userRepository.existsByAccountNumber(depositRequest.getAccountNumber())) {
            User foundUser = userRepository.findByAccountNumber(depositRequest.getAccountNumber());
            foundUser.setAccountBalance(foundUser.getAccountBalance().add(depositRequest.getAmount()));
            userRepository.save(foundUser);

            TransactionsDto transactionsDto = TransactionsDto.builder()
                    .AccountNumber(foundUser.getAccountNumber())
                    .amount(depositRequest.getAmount())
                    .transactionType("Deposit")
                    .build();
            transactionService.saveTransaction(transactionsDto);

            return BankResponse.builder()
                    .ResponseCode(AccountUtil.Deposit_Done_Successfully_Code)
                    .ResponseMessage(AccountUtil.Deposit_Done_Successfully_Message)
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(foundUser.getAccountBalance())
                            .accountName(foundUser.getFirstName()+" " +foundUser.getLastName())
                            .accountNumber(foundUser.getAccountNumber()).build()).build();
        }
        else{
            return BankResponse.builder()
                    .ResponseCode(AccountUtil.Account_Doesnt_Exists_Code)
                    .ResponseMessage(AccountUtil.Account_Doesnt_Exists_Message)
                    .accountInfo(null).build();
        }



    }

    @Override
    public BankResponse withdraw(WithdrawRequest withdrawRequest) {
        if(userRepository.existsByAccountNumber(withdrawRequest.getAccountNumber())) {
            User foundUser = userRepository.findByAccountNumber(withdrawRequest.getAccountNumber());
            if(withdrawRequest.getAmount().compareTo(foundUser.getAccountBalance()) > 0) {
                return BankResponse.builder()
                        .ResponseCode(AccountUtil.Insufficient_Funds_Code)
                        .ResponseMessage(AccountUtil.Insufficient_Funds_Message)
                        .accountInfo(AccountInfo.builder()
                                .accountBalance(foundUser.getAccountBalance())
                                .accountName(foundUser.getFirstName()+" " +foundUser.getLastName())
                                .accountNumber(foundUser.getAccountNumber()).build()).build();


            }
            else {

                foundUser.setAccountBalance(foundUser.getAccountBalance().subtract(withdrawRequest.getAmount()));
                userRepository.save(foundUser);

                TransactionsDto transactionsDto = TransactionsDto.builder()
                        .AccountNumber(foundUser.getAccountNumber())
                        .amount(withdrawRequest.getAmount())
                        .transactionType("Withdraw")
                        .build();
                transactionService.saveTransaction(transactionsDto);

                return BankResponse.builder()
                        .ResponseCode(AccountUtil.Withdraw_Done_Successfully_Code)
                        .ResponseMessage(AccountUtil.Withdraw_Done_Successfully_Message)
                        .accountInfo(AccountInfo.builder()
                                .accountBalance(foundUser.getAccountBalance())
                                .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                                .accountNumber(foundUser.getAccountNumber()).build()).build();
            }
        }
        else{
            return BankResponse.builder()
                    .ResponseCode(AccountUtil.Account_Doesnt_Exists_Code)
                    .ResponseMessage(AccountUtil.Account_Doesnt_Exists_Message)
                    .accountInfo(null).build();
        }

    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        if(userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber()) && userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber()) ) {
            User sourceUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
            User destinationUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());

            if(transferRequest.getAmount().compareTo(sourceUser.getAccountBalance()) > 0) {
                return BankResponse.builder()
                        .ResponseCode(AccountUtil.Insufficient_Funds_Code)
                        .ResponseMessage(AccountUtil.Insufficient_Funds_Message)
                        .accountInfo(AccountInfo.builder()
                                .accountBalance(sourceUser.getAccountBalance())
                                .accountName(sourceUser.getFirstName()+" " +sourceUser.getLastName())
                                .accountNumber(sourceUser.getAccountNumber()).build()).build();
            }
            else {
                sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(transferRequest.getAmount()));
                destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(transferRequest.getAmount()));
                userRepository.save(sourceUser);
                userRepository.save(destinationUser);

                TransactionsDto desTransactionsDto = TransactionsDto.builder()
                        .AccountNumber(destinationUser.getAccountNumber())
                        .amount(transferRequest.getAmount())
                        .transactionType("Receive")
                        .build();
                transactionService.saveTransaction(desTransactionsDto);

                TransactionsDto sourceTransactionsDto = TransactionsDto.builder()
                        .AccountNumber(sourceUser.getAccountNumber())
                        .amount(transferRequest.getAmount())
                        .transactionType("Transfer")
                        .build();
                transactionService.saveTransaction(sourceTransactionsDto);

                EmailDetails SourceEmailDetails = EmailDetails.builder()
                        .recipient(sourceUser.getEmail())
                        .subject("Money Transfer")
                        .messageBody("You Has Transferred " +transferRequest.getAmount() +" To Account number : " +transferRequest.getDestinationAccountNumber()
                        ).build();

                emailService.sendEmailAlert(SourceEmailDetails);

                EmailDetails desEmailDetails = EmailDetails.builder()
                        .recipient(destinationUser.getEmail())
                        .subject("Money Transfer")
                        .messageBody("You Has Received " +transferRequest.getAmount() +" from Account number :" +transferRequest.getSourceAccountNumber()
                        ).build();

                emailService.sendEmailAlert(desEmailDetails);


                return BankResponse.builder()
                        .ResponseCode(AccountUtil.Transfer_Done_Successfully_Code)
                        .ResponseMessage(AccountUtil.Transfer_Done_Successfully_Message)
                        .accountInfo(AccountInfo.builder()
                                .accountBalance(destinationUser.getAccountBalance())
                                .accountName(destinationUser.getFirstName() + " " + destinationUser.getLastName())
                                .accountNumber(destinationUser.getAccountNumber()).build()).build();
            }
        }
        else{
            return BankResponse.builder()
                    .ResponseCode(AccountUtil.Account_Doesnt_Exists_Code)
                    .ResponseMessage(AccountUtil.Account_Doesnt_Exists_Message)
                    .accountInfo(null).build();
        }

    }
}
