package controller;

import com.despegar.http.client.*;
import com.despegar.sparkjava.test.SparkServer;
import domain.Account;
import domain.PaymentDTO;
import domain.TransferDTO;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static Utils.JsonUtils.dataToJson;
import static Utils.JsonUtils.jsonToData;
import static org.junit.Assert.assertEquals;


public class PaymentControllerTest {
    private static long VALUE = 1000L;
    private UUID id1;
    private UUID id2;

    public static class TestController implements SparkApplication {
        @Override
        public void init() {
            App.start();
        }
    }

    @ClassRule
    public static SparkServer<TestController> testServer = new SparkServer<>(TestController.class, 4567);

    @Before
    public void setUp() throws Exception {
        final Account firstAccount = post("/payment", new Account(null, VALUE));
        final Account secondAccount = post("/payment", new Account(null, 0L));
        id1 = firstAccount.getId();
        id2 = secondAccount.getId();
    }

    @Test
    public void testGet() throws HttpClientException {
        Account account = get("/payment/" + id1);
        assertEquals(id1, account.getId());
    }

    @Test
    public void testCreate() throws HttpClientException {
        Account newAccount =  post("/payment", new Account(null, VALUE));
        assertEquals(VALUE, newAccount.getValue());
    }

    @Test
    public void testUpdate() throws HttpClientException {
        Account updatedAccount = put("/payment", new PaymentDTO(id1, -500L));
        assertEquals(id1, updatedAccount.getId());
        assertEquals(500L, updatedAccount.getValue());
        updatedAccount = put("/payment", new PaymentDTO(id1, 500L));
        assertEquals(id1, updatedAccount.getId());
        assertEquals(1000L, updatedAccount.getValue());
    }

    @Test
    public void testTransfer() throws HttpClientException {
        TransferDTO transfer = new TransferDTO(id1, id2, 200L);
        postForTransfer("/transfer", transfer);

        Account account1 = get("/payment/" + id1);
        Account account2 = get("/payment/" + id2);
        assertEquals(800L, account1.getValue());
        assertEquals(200L, account2.getValue());
    }

    @Test
    public void simpleSyncTest() throws HttpClientException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 1000; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        TransferDTO transfer = new TransferDTO(id1, id2, 1L);
                        PostMethod resp = testServer.post("/transfer", dataToJson(transfer), false);
                        testServer.execute(resp);
                    } catch (Exception ex) {
                        System.out.println("failed execution");
                    }
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        Account account1 = get("/payment/" + id1);
        Account account2 = get("/payment/" + id2);
        assertEquals(0L, account1.getValue());
        assertEquals(1000L, account2.getValue());
    }

    private Account get(String params) throws HttpClientException {
        GetMethod resp = testServer.get(params, false);
        HttpResponse execute = testServer.execute(resp);
        return jsonToData(new String(execute.body()), Account.class);
    }

    private Account post(String path, Object payload) throws HttpClientException {
        PostMethod resp = testServer.post(path, dataToJson(payload), false);
        HttpResponse execute = testServer.execute(resp);
        return jsonToData(new String(execute.body()), Account.class);
    }

    private Account put(String path, Object payload) throws HttpClientException {
        PutMethod resp = testServer.put(path, dataToJson(payload), false);
        HttpResponse execute = testServer.execute(resp);
        return jsonToData(new String(execute.body()), Account.class);
    }

    private String postForTransfer(String path, Object payload) throws HttpClientException {
        PostMethod resp = testServer.post(path, dataToJson(payload), false);
        HttpResponse execute = testServer.execute(resp);
        return new String(execute.body());
    }
}
