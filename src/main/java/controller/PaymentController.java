package controller;

import domain.Account;
import domain.PaymentDTO;
import domain.TransferDTO;
import spark.Route;

import java.util.UUID;

import static Utils.JsonUtils.jsonToData;
import static controller.App.paymentService;

public class PaymentController {

    public static Route get = (req, res) ->
            paymentService.getAccount(UUID.fromString(req.params(":id")));

    public static Route put = (req, res) ->
            paymentService.updateAccount(jsonToData(req.body(), PaymentDTO.class));

    public static Route post = (req, res) ->
            paymentService.createAccount(jsonToData(req.body(), Account.class));

    public static Route postForTransfer = (req, res) ->
            paymentService.transfer(jsonToData(req.body(), TransferDTO.class));
}
