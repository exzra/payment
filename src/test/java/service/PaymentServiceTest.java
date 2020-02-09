package service;

import domain.Account;
import domain.PaymentDTO;
import domain.TransferDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class PaymentServiceTest {

    PaymentService service = new PaymentServiceImpl();
    private UUID id1;

    @Before
    public void setUp() {
        final Account account = service.createAccount(new Account(null, 1000L));
        id1 = account.getId();
    }

    @Test(expected = RuntimeException.class)
    public void testCreateWithIncorrectValue() {
        service.createAccount(new Account(null, -1000L));
    }

    @Test(expected = RuntimeException.class)
    public void testGetNull() {
        service.getAccount(null);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateWithIncorrectValue() {
        service.updateAccount(new PaymentDTO(id1, -2000L));
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidTransfer() {
        service.transfer(new TransferDTO(id1, UUID.randomUUID(),  -2000L));
    }
}
