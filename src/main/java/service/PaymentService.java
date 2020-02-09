package service;

import java.util.UUID;

public interface PaymentService {
    void updateAccount(UUID accountId, Long paymentValue);

    UUID createAccount(Long initialValue);
}
