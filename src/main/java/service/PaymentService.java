package service;

import domain.Account;
import domain.PaymentDTO;

import java.util.UUID;

public interface PaymentService {
    Account updateAccount(PaymentDTO payment);

    Account createAccount(Account account);

    Account getAccount(UUID accountId);
}
