package service;

import domain.Account;
import domain.PaymentDTO;
import domain.TransferDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PaymentServiceImpl implements PaymentService {

    /**
     * check README for implementation details and solution explanation
     */

    private final Map<UUID, Account> storage = new HashMap<>();

    @Override
    public Account updateAccount(PaymentDTO payment) {
        synchronized (storage) {
            Account account = storage.get(payment.getId());
            if (account.getValue() + payment.getDiff() >= 0) {
                account.setValue(account.getValue() + payment.getDiff());
                storage.put(payment.getId(), account);
                return account;
            } else {
                throw new RuntimeException("not enough money");
            }
        }
    }

    @Override
    public String transfer(TransferDTO transfer) {
        synchronized (storage) {
            Account sender = storage.get(transfer.getSenderID());
            Account receiver = storage.get(transfer.getReceiverID());

            if (sender.getValue() - transfer.getValue() >= 0) {
                sender.setValue(sender.getValue() - transfer.getValue());
                receiver.setValue(receiver.getValue() + transfer.getValue());

                storage.put(transfer.getSenderID(), sender);
                storage.put(transfer.getReceiverID(), receiver);
                return "Transfer successfully completed from " + transfer.getSenderID() + " to " + transfer.getReceiverID();
            } else {
                throw new RuntimeException("not enough money");
            }
        }
    }

    @Override
    public Account createAccount(Account account) {
        Account newAccount = new Account(UUID.randomUUID(), account.getValue());
        storage.put(newAccount.getId(), newAccount);
        return newAccount;
    }

    @Override
    public Account getAccount(UUID accountId) {
        synchronized (storage) {
            return storage.get(accountId);
        }
    }
}
