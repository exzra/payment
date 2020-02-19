package service;

import domain.Account;
import domain.PaymentDTO;
import domain.TransferDTO;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PaymentServiceImpl implements PaymentService {

    /**
     * check README for implementation details and solution explanation
     */

    private final Map<UUID, Account> storage = new ConcurrentHashMap<>();

    @Override
    public Account updateAccount(PaymentDTO payment) {
        Account account = storage.get(payment.getId());
        account.getAccountLock().writeLock().lock();
        try {
            if (account.getValue() + payment.getDiff() >= 0) {
                account.setValue(account.getValue() + payment.getDiff());
                storage.put(payment.getId(), account);
                return account;
            } else {
                throw new RuntimeException("not enough money");
            }
        } finally {
            account.getAccountLock().writeLock().unlock();
        }
    }

    @Override
    public String transfer(TransferDTO transfer) {
        if (transfer.getValue() < 0) {
            throw new RuntimeException("not enough money");
        }
        Account sender = storage.get(transfer.getSenderID());
        Account receiver = storage.get(transfer.getReceiverID());

        try {
            sender.getAccountLock().writeLock().lock();
            receiver.getAccountLock().writeLock().lock();

            if (sender.getValue() - transfer.getValue() >= 0) {
                sender.setValue(sender.getValue() - transfer.getValue());
                receiver.setValue(receiver.getValue() + transfer.getValue());

                storage.put(transfer.getSenderID(), sender);
                storage.put(transfer.getReceiverID(), receiver);
                return "Transfer successfully completed from " + transfer.getSenderID() + " to " + transfer.getReceiverID();
            } else {
                throw new RuntimeException("not enough money");
            }
        } finally {
            sender.getAccountLock().writeLock().unlock();
            receiver.getAccountLock().writeLock().unlock();
        }
    }

    @Override
    public Account createAccount(Account account) {
        Account newAccount = new Account(UUID.randomUUID(), account.getValue());
        if (account.getValue() < 0) {
            throw new RuntimeException("Invalid cash value");
        }
        storage.put(newAccount.getId(), newAccount);
        return newAccount;
    }

    @Override
    public Account getAccount(UUID accountId) {
        if (accountId == null) {
            throw new RuntimeException("Invalid uuid");
        }
        return storage.get(accountId);
    }
}
