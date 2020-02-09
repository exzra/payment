package controller;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Account;
import domain.PaymentDTO;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class PaymentControllerTest {

    private static UUID ID = UUID.randomUUID();
    private static long VALUE = 1000L;
    private ObjectMapper mapper = new ObjectMapper();

    public static class TestController implements SparkApplication {
        @Override
        public void init() {
            App.start();
        }
    }

    @ClassRule
    public static SparkServer<TestController> testServer = new SparkServer<>(TestController.class, 4567);

    @Test
    public void testGet() throws HttpClientException {
        Account account = get("/payment?id=" + ID);
        assertEquals(ID, account.getId());
    }

    @Test
    public void testPost() throws HttpClientException {
        Account newAccount =  post("/payment", new Account(ID, VALUE));
        assertEquals(ID, newAccount.getId());
        assertEquals(VALUE, newAccount.getValue());
    }

    @Test
    public void testPut() throws HttpClientException {
        Account updatedAccount = put("/payment?id=" + ID, dataToJson(new PaymentDTO(-500L)));
        assertEquals(ID, updatedAccount.getId());
        assertEquals(500L, updatedAccount.getValue());
    }

    private Account get(String params) throws HttpClientException {
        GetMethod resp = testServer.get(params, false);
        HttpResponse execute = testServer.execute(resp);
        return jsonToData(new String(execute.body()));
    }

    private Account post(String path, Object payload) throws HttpClientException {
        PostMethod resp = testServer.post(path, dataToJson(payload), false);
        HttpResponse execute = testServer.execute(resp);
        return jsonToData(new String(execute.body()));
    }

    private Account put(String params, Object payload) throws HttpClientException {
        GetMethod resp = testServer.get(params, false);
        HttpResponse execute = testServer.execute(resp);
        return jsonToData(new String(execute.body()));
    }

    private Account jsonToData(String body) {
        try {
            return mapper.readValue(body, Account.class);
        } catch (IOException e) {
            throw new RuntimeException("IOException while parsing the result");
        }
    }

    public String dataToJson(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (IOException e) {
            throw new RuntimeException("IOEXception while mapping object (" + data + ") to JSON. " + e.getMessage());
        }
    }
}
