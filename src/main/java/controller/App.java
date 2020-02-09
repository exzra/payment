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
    //private static final Logger LOG = Logger.getLogger(App.class);

    public static PaymentService paymentService;

    static {
        paymentService = new PaymentServiceImpl();
    }

    Map<UUID, Long> accounts = new HashMap<>();

    public static void main(String[] args) {
        port(8080);
        start();
    }

    public static void start() {
//        JsonUtils.registerModules();

       // LOG.info("Initializing routes");
        establishRoutes();
    }

    private static void establishRoutes() {
        get("/payment", (req, res) -> "Hello");

        post("/payment", (req, res) -> req.attribute("id"));

        put("/payment", (req, res) -> req.attributes());
    }
}
