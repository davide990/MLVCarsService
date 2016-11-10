/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upem.mlvCars.services.bank;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
public class BankServiceClient {

    public static void withdrawMoneyFromUserAccount(String iban, int amount) {
        BankService_Service sv = new BankService_Service();
        BankService service = sv.getBankServicePort();
        service.withdrawMoneyFrom(iban, amount);
    }

}
