package controller;

import service.PaymentService;
import service.PaymentServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;
import static spark.route.HttpMethod.put;

public class App {

    private static final String PATH = "/payment";
    private static final String TRANSFER_PATH = "/transfer";

    public static PaymentService paymentService;

    static {
        paymentService = new PaymentServiceImpl();
    }

    public static void main(String[] args) {
        port(8080);
        start();
    }

    public static void start() {
        establishRoutes();
    }

    private static void establishRoutes() {
        get(PATH+ "/:id", PaymentController.get);
        post(PATH, PaymentController.post);
        put(PATH, PaymentController.put);
        post(TRANSFER_PATH, PaymentController.postForTransfer);
    }
}
