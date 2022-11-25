<<<<<<< Updated upstream
package com.diy.software.test.iteration2;

import com.diy.hardware.external.CardIssuer;
import com.diy.software.payment.CreditPayment;
import com.diy.software.payment.DebitPayment;
import com.jimmyselectronics.opeechee.*;
import com.jimmyselectronics.opeechee.Card.CardData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DebitPaymentTest {
    Card testDebitCard = new Card("Debit", "0000000000000000", "Ben", "567", "1234", true, true);
    Card testNoChipDebitCard = new Card("Debit", "1000000000000001", "Joe", "111", "0000", false, false);
    Card testCreditCard = new Card("Visa", "1111000000000000", "Sam", "763", "1234", true, true);
    CardData data;
    CardIssuer bank;
    long holdNumber;
    CardReader testReader = new CardReader();
    String pin = "1234";
    Calendar expiry = new GregorianCalendar(2024, 9, 31); //Creates an expiry date to pass into the account creater.


    boolean flag;

    @Before
    public void setup() throws Exception {
        bank = new CardIssuer("ATB", 5000);
        data = testDebitCard.insert("1234");
        bank.addCardData(data.getNumber(), data.getCardholder(), expiry, data.getCVV(), 2000.00); //This "creates an account" for the card with $2000 available credit.
        bank.addCardData(testNoChipDebitCard.number, testNoChipDebitCard.cardholder, expiry, testNoChipDebitCard.cvv, 2500);
        testReader.plugIn();
        testReader.turnOn();
    }

    @Test
    public void powerUpTesting() {
        Assert.assertTrue(testReader.isPluggedIn()); // is the reader plugged in? should be yes
        Assert.assertTrue(testReader.isPoweredUp()); // is the reader turned on? should be yes
        testReader.turnOff();
        Assert.assertFalse(testReader.isPoweredUp()); // is the reader turned on?
    }

    @Test
    public void ConstructorTesting1() throws IOException {
        DebitPayment newpay2 = new DebitPayment(100, testDebitCard, testReader, "1234", bank);
        Assert.assertNotNull(newpay2);
    }

    @Test
    public void payWithCreditCard() throws IOException {
        DebitPayment newPay = new DebitPayment(100, testCreditCard, testReader, "1234", bank);
        try {
            newPay.insertCard("1234");
        } catch (IllegalArgumentException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    @Test
    public void ConstructorTesting2() {
        try {
            new DebitPayment(100, null, testReader, pin, bank);
        }
        catch (NullPointerException | IOException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void ConstructorTesting3() {
        CardData data = null;
        DebitPayment newPay = new DebitPayment(100, data, bank);
        flag = newPay.payForTotal();
    }

    @Test  //testing to make sure payfortotal works
    public void ConstructorTesting4() throws IOException {
        CardData data = testDebitCard.insert("1234");
        DebitPayment newPay = new DebitPayment(100, data, bank);
        flag = newPay.payForTotal();
    }

    @Test
    public void InsufficientFunds() {
        //This test will check that payForTotal() correctly returns false if the customer tries to make a payment larger than their account balance
        DebitPayment newPay = new DebitPayment();
        newPay.setCardData(data);
        newPay.setCardIssuer(bank);
        flag = newPay.payForTotal(2001);
        assertFalse(flag);
    }

    @Test
    public void CorrectInsert() throws IOException {
        // wrapped the test in a while !flag so that it can repeat the test
        // if it failed due to the random hardware failure chance.
        while (!flag) {
            DebitPayment newPay = new DebitPayment();
            newPay.setCard(testDebitCard);
            newPay.setReader(testReader);
            newPay.setCardIssuer(bank);
            newPay.setTotal(300);
            newPay.insertCard("1234");
            flag = newPay.payForTotal();
        }
        assertTrue(flag);
    }

    @Test
    public void ThreeFalsePINS() throws IOException {
        DebitPayment newPay = new DebitPayment();
        newPay.setCard(testDebitCard);
        newPay.setReader(testReader);
        newPay.setCardIssuer(bank);
        newPay.setTotal(300);
        flag = true;
        try {
            newPay.insertCard("3333");
        } catch (InvalidPINException e) {
            flag = false;
        }
        assertFalse(flag);
        flag = true;
        try {
            newPay.insertCard("2222");
        } catch (InvalidPINException e) {
            flag = false;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        assertFalse(flag);
        flag = true;
        try {
            newPay.insertCard("1111");
        } catch (InvalidPINException e) {
            flag = false;
        }
        assertFalse(flag);
        try {
            newPay.insertCard("1234"); //Even though the PIN is correct here, it should still fail because the card should be blocked
        } catch (BlockedCardException e) {
            flag = true;
        } catch (Exception e) {
            System.out.println("Some exception encountered other than blocked card." + e.toString());
        }
        assertTrue(flag);
    }

    @Test
    public void NoChipPayment() throws IOException {
        DebitPayment newPay = new DebitPayment();
        newPay.setCard(testNoChipDebitCard);
        newPay.setReader(testReader);
        newPay.setTotal(300);
        newPay.insertCard("5555"); //Even though this is the correct payment, the insert should fail since the card has no chip.
        flag = newPay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void PayWithUndefinedObject() {
        DebitPayment newpay = new DebitPayment();
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void NegativeTotal() throws IOException {
        DebitPayment newpay = new DebitPayment();
        newpay.setCard(testDebitCard);
        newpay.setReader(testReader);
        newpay.setTotal(300);
        newpay.insertCard("1234");
        newpay.setTotal(-1); //Even though the correct pin was provided, payForTotal() will fail because negative totals aren't allowed
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void ConstructorWithNoChip() throws IOException {
        CreditPayment newpay = new CreditPayment(100, testNoChipDebitCard, testReader, "5555", bank); //Construction will fail because the card has no chip
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void AlternateInsert() throws IOException {
        // wrapped the test in a while !flag so that it can repeat the test
        // if it failed due to the random hardware failure chance.
        while (!flag) {
            DebitPayment newpay = new DebitPayment();
            newpay.setReader(testReader);
            newpay.setCardIssuer(bank);
            newpay.insertCard(testDebitCard, "1234");
            flag = newpay.payForTotal(300);
        }
        assertTrue(flag);
    }
}
=======
package com.diy.software.test.iteration2;

import com.diy.hardware.external.CardIssuer;
import com.diy.software.payment.CreditPayment;
import com.diy.software.payment.DebitPayment;
import com.jimmyselectronics.opeechee.*;
import com.jimmyselectronics.opeechee.Card.CardData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DebitPaymentTest {
    Card testDebitCard = new Card("Debit", "0000000000000000", "Ben", "567", "1234", true, true);
    Card testNoChipDebitCard = new Card("Debit", "1000000000000001", "Joe", "111", "0000", false, false);
    Card testCreditCard = new Card("Visa", "1111000000000000", "Sam", "763", "1234", true, true);
    CardData data;
    CardIssuer bank;
    CardReader testReader = new CardReader();
    String pin = "1234";
    Calendar expiry = new GregorianCalendar(2024, 9, 31); //Creates an expiry date to pass into the account creater.


    boolean flag;

    @Before
    public void setup() throws Exception {
        bank = new CardIssuer("ATB", 5000);
        data = testDebitCard.insert("1234");
        bank.addCardData(data.getNumber(), data.getCardholder(), expiry, data.getCVV(), 2000.00); //This "creates an account" for the card with $2000 available credit.
        bank.addCardData(testNoChipDebitCard.number, testNoChipDebitCard.cardholder, expiry, testNoChipDebitCard.cvv, 2500);
        testReader.plugIn();
        testReader.turnOn();
    }

    @Test
    public void powerUpTesting() {
        Assert.assertTrue(testReader.isPluggedIn()); // is the reader plugged in? should be yes
        Assert.assertTrue(testReader.isPoweredUp()); // is the reader turned on? should be yes
        testReader.turnOff();
        Assert.assertFalse(testReader.isPoweredUp()); // is the reader turned on?
    }

    @Test
    public void ConstructorTesting1() throws IOException {
        DebitPayment newpay2 = new DebitPayment(100, testDebitCard, testReader, "1234", bank);
        Assert.assertNotNull(newpay2);
    }

    @Test
    public void payWithCreditCard() throws IOException {
        DebitPayment newPay = new DebitPayment(100, testCreditCard, testReader, "1234", bank);
        try {
            newPay.insertCard("1234");
        } catch (IllegalArgumentException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    @Test
    public void ConstructorTesting2() {
        try {
            new DebitPayment(100, null, testReader, pin, bank);
        }
        catch (NullPointerException | IOException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void ConstructorTesting3() {
        CardData data = null;
        DebitPayment newPay = new DebitPayment(100, data, bank);
        flag = newPay.payForTotal();
    }

    @Test  //testing to make sure payfortotal works
    public void ConstructorTesting4() throws IOException {
        CardData data = testDebitCard.insert("1234");
        DebitPayment newPay = new DebitPayment(100, data, bank);
        flag = newPay.payForTotal();
    }

    @Test
    public void InsufficientFunds() {
        //This test will check that payForTotal() correctly returns false if the customer tries to make a payment larger than their account balance
        DebitPayment newPay = new DebitPayment();
        newPay.setCardData(data);
        newPay.setCardIssuer(bank);
        flag = newPay.payForTotal(2001);
        assertFalse(flag);
    }

    @Test
    public void CorrectInsert() throws IOException {
        // wrapped the test in a while !flag so that it can repeat the test
        // if it failed due to the random hardware failure chance.
        while (!flag) {
            DebitPayment newPay = new DebitPayment();
            newPay.setCard(testDebitCard);
            newPay.setReader(testReader);
            newPay.setCardIssuer(bank);
            newPay.setTotal(300);
            newPay.insertCard("1234");
            flag = newPay.payForTotal();
        }
        assertTrue(flag);
    }

    @Test
    public void ThreeFalsePINS() throws IOException {
        DebitPayment newPay = new DebitPayment();
        newPay.setCard(testDebitCard);
        newPay.setReader(testReader);
        newPay.setCardIssuer(bank);
        newPay.setTotal(300);
        flag = true;
        try {
            newPay.insertCard("3333");
        } catch (InvalidPINException e) {
            flag = false;
        }
        assertFalse(flag);
        flag = true;
        try {
            newPay.insertCard("2222");
        } catch (InvalidPINException e) {
            flag = false;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        assertFalse(flag);
        flag = true;
        try {
            newPay.insertCard("1111");
        } catch (InvalidPINException e) {
            flag = false;
        }
        assertFalse(flag);
        try {
            newPay.insertCard("1234"); //Even though the PIN is correct here, it should still fail because the card should be blocked
        } catch (BlockedCardException e) {
            flag = true;
        } catch (Exception e) {
            System.out.println("Some exception encountered other than blocked card." + e.toString());
        }
        assertTrue(flag);
    }

    @Test
    public void NoChipPayment() throws IOException {
        DebitPayment newPay = new DebitPayment();
        newPay.setCard(testNoChipDebitCard);
        newPay.setReader(testReader);
        newPay.setTotal(300);
        newPay.insertCard("5555"); //Even though this is the correct payment, the insert should fail since the card has no chip.
        flag = newPay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void PayWithUndefinedObject() {
        DebitPayment newpay = new DebitPayment();
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void NegativeTotal() throws IOException {
        DebitPayment newpay = new DebitPayment();
        newpay.setCard(testDebitCard);
        newpay.setReader(testReader);
        newpay.setTotal(300);
        newpay.insertCard("1234");
        newpay.setTotal(-1); //Even though the correct pin was provided, payForTotal() will fail because negative totals aren't allowed
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void ConstructorWithNoChip() throws IOException {
        CreditPayment newpay = new CreditPayment(100, testNoChipDebitCard, testReader, "5555", bank); //Construction will fail because the card has no chip
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void AlternateInsert() throws IOException {
        // wrapped the test in a while !flag so that it can repeat the test
        // if it failed due to the random hardware failure chance.
        while (!flag) {
            DebitPayment newpay = new DebitPayment();
            newpay.setReader(testReader);
            newpay.setCardIssuer(bank);
            newpay.insertCard(testDebitCard, "1234");
            flag = newpay.payForTotal(300);
        }
        assertTrue(flag);
    }
}
>>>>>>> Stashed changes
