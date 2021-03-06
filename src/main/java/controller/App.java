package controller;

import Utils.JsonTransformer;
import service.PaymentService;
import service.PaymentServiceImpl;

import static spark.Spark.*;

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
        get(PATH+ "/:id", PaymentController.get, new JsonTransformer());
        post(PATH, PaymentController.post, new JsonTransformer());
        put(PATH, PaymentController.put, new JsonTransformer());
        post(TRANSFER_PATH, PaymentController.postForTransfer, new JsonTransformer());
    }
}
