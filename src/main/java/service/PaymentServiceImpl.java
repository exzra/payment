package service;

import domain.Account;
import domain.PaymentDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaymentServiceImpl implements PaymentService {

    Map<UUID, Account> accounts = new HashMap<>();

    @Override
    public Account updateAccount(PaymentDTO payment) {
        Account account = accounts.get(payment.getId());
        account.setValue(account.getValue() + payment.getDiff());
        accounts.put(payment.getId(), account);
        return account;
    }

    @Override
    public Account createAccount(Account account) {
        Account newAccount = new Account(UUID.randomUUID(), account.getValue());
        accounts.put(newAccount.getId(), newAccount);
        return newAccount;
    }

    @Override
    public Account getAccount(UUID accountId) {
        return accounts.get(accountId);
    }
}
