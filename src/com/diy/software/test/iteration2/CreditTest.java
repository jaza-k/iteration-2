package com.diy.software.test.iteration2;

import com.diy.hardware.external.CardIssuer;
import com.diy.software.payment.CreditPayment;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.Card.CardData;
import com.jimmyselectronics.opeechee.CardReader;
import com.jimmyselectronics.opeechee.InvalidPINException;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreditTest {
    public static void main(String[] args) throws InvalidPINException {
        //The main function is only here for demonstration purposes and should be removed from the final submission.
        Card testcard = new Card("Visa", "0000000000000000", "Sam", "763", "1234", true, true);
        CardIssuer bank = new CardIssuer("ATB", 5000);
        long holdnum;

        //System.out.println("THIS IS A TEST THIS IS A TEST THIS IS A TEST THIS IS A TEST");
        try {
            CardReader testreader = new CardReader();
            CardData data = testcard.insert("1234");
            Calendar expiry = new GregorianCalendar(2024, 9, 31); //Creates an expiry date to pass into the account creater.
            bank.addCardData(data.getNumber(), data.getCardholder(), expiry, data.getCVV(), 2000.00); //This "creates an account" for the card with $2000 available credit.
            CreditPayment newpay = new CreditPayment(100, data, bank);
            boolean flag;

            System.out.println(expiry.getTime()); //Note that setting the "month" field in a gregorian calendar as 9 translates to october instead of september
            //that implies that january = 0 and december = 11. But for some reason only months start from 0, the year and day both start from 1 (:-S)


            flag = newpay.payForTotal(); //Attempts to pay for a $1200 transaction with the card
            if (flag) System.out.println("First transaction successful");
            else System.out.println("First transaction failed");

            testreader.plugIn();
            testreader.turnOn();
            CreditPayment newpay2 = new CreditPayment(100, testcard, testreader, "1234", bank);
            flag = newpay2.payForTotal();
            if (flag) System.out.println("Second transaction successful");
            else System.out.println("Second transaction failed");

            CreditPayment newpay3 = new CreditPayment();
            newpay3.setCardData(data);
            newpay3.setCardIssuer(bank);
            flag = newpay3.payForTotal(200);
            if (flag) System.out.println("Third transaction successful");
            else System.out.println("Third transaction failed");

            flag = newpay3.payForTotal(1601); //The customer will only have 1600 in their account at this time, and the transaction will fail
            if (flag) System.out.println("Fourth transaction successful");
            else System.out.println("Fourth transaction failed");

        } catch (Exception e) {
            System.out.println("Some exception was thrown.");
        }
    }
}