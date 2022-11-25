<<<<<<< HEAD
package com.diy.software.test.iteration1;

import com.diy.hardware.external.CardIssuer;
import com.diy.software.payment.CreditPayment;
import com.jimmyselectronics.opeechee.*;
import com.jimmyselectronics.opeechee.Card.CardData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreditPaymentTest {
    Card testcard = new Card("Visa", "0000000000000000", "Sam", "763", "1234", true, true);
    Card nochipcard = new Card("Visa", "0000000000000001", "Lucinda", "333", "5555", false, false);
    CardData data;
    CardIssuer bank;
    long holdnum;
    CardReader testreader = new CardReader();
    String pin = "1234";
    Calendar expiry = new GregorianCalendar(2024, 9, 31); //Creates an expiry date to pass into the account creater.


    boolean flag;

    @Before
    public void setUp() throws Exception {
        bank = new CardIssuer("ATB", 5000);
        data = testcard.insert("1234");
        bank.addCardData(data.getNumber(), data.getCardholder(), expiry, data.getCVV(), 2000.00); //This "creates an account" for the card with $2000 available credit.
        bank.addCardData("0000000000000001", "Lucinda", expiry, "333", 2000);
        testreader.plugIn();
        testreader.turnOn();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void powerUpTesting() {
        Assert.assertTrue(testreader.isPluggedIn()); // is the reader plugged in? should be yes
        Assert.assertTrue(testreader.isPoweredUp()); // is the reader turned on? should be yes
        testreader.turnOff();
        Assert.assertFalse(testreader.isPoweredUp()); // is the reader turned on?
    }

//	@Test
//	public void cardInsertedTest1() throws IOException {
//		testreader.turnOn();
//		testreader.enable();
//		testreader.insert(testcard, pin);
//	}

    @Test
    public void ConstructorTesting1() throws IOException {
        CreditPayment newpay2 = new CreditPayment(100, testcard, testreader, "1234", bank);
        Assert.assertNotNull(newpay2);
    }

    @Test
    public void ConstructorTesting2() throws IOException {
        new CreditPayment(100, null, testreader, pin, bank);
    }

    @Test  // testing to make sure we return false
    public void ConstructorTesting3() throws IOException {
        CardData data = null;
        CreditPayment newPay = new CreditPayment(100, data, bank);
        flag = newPay.payForTotal();
    }

    @Test  //testing to make sure payfortotal works
    public void ConstructorTesting4() throws IOException {
        CardData data = testcard.insert("1234");
        CreditPayment newPay = new CreditPayment(100, data, bank);
        flag = newPay.payForTotal();
    }

    @Test
    public void InsufficientFunds() {
        //This test will check that payForTotal() correctly returns false if the customer tries to make a payment larger than their account balance
        CreditPayment newpay = new CreditPayment();
        newpay.setCardData(data);
        newpay.setCardIssuer(bank);
        flag = newpay.payForTotal(2001);
        assertFalse(flag);
    }

    @Test
    public void CorrectInsert() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setCard(testcard);
        newpay.setReader(testreader);
        newpay.setCardIssuer(bank);
        newpay.setTotal(300);
        newpay.insertCard("1234");
        flag = newpay.payForTotal();
        assertTrue(flag);
    }

    @Test
    public void ThreeFalsePINS() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setCard(testcard);
        newpay.setReader(testreader);
        newpay.setCardIssuer(bank);
        newpay.setTotal(300);
        flag = true;
        try {
            newpay.insertCard("3333");
        } catch (InvalidPINException e) {
            flag = false;
        }
        assertFalse(flag);
        flag = true;
        try {
            newpay.insertCard("2222");
        } catch (InvalidPINException e) {
            flag = false;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        assertFalse(flag);
        flag = true;
        try {
            newpay.insertCard("1111");
        } catch (InvalidPINException e) {
            flag = false;
        }
        assertFalse(flag);
        try {
            newpay.insertCard("1234"); //Even though the PIN is correct here, it should still fail because the card should be blocked
        } catch (BlockedCardException e) {
            flag = true;
        } catch (Exception e) {
            System.out.println("Some exception encountered other than blocked card." + e.toString());
        }
        assertTrue(flag);
    }

    @Test
    public void NoChipPayment() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setCard(nochipcard);
        newpay.setReader(testreader);
        newpay.setTotal(300);
        newpay.insertCard("5555"); //Even though this is the correct payment, the insert should fail since the card has no chip.
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void PayWithUndefinedObject() {
        CreditPayment newpay = new CreditPayment();
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void NegativeTotal() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setCard(testcard);
        newpay.setReader(testreader);
        newpay.setTotal(300);
        newpay.insertCard("1234");
        newpay.setTotal(-1); //Even though the correct pin was provided, payForTotal() will fail because negative totals aren't allowed
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void ConstructorWithNoChip() throws IOException {
        CreditPayment newpay = new CreditPayment(100, nochipcard, testreader, "5555", bank); //Construction will fail because the card has no chip
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void AlternateInsert() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setReader(testreader);
        newpay.setCardIssuer(bank);
        newpay.insertCard(testcard, "1234");
        flag = newpay.payForTotal(300);
        assertTrue(flag);
    }

    // for chip failure exception, creditpayment, force a chipfailure exception?
    // might not need to deal with this, this might be on the hardware side or need a % chance of failure

}
=======
package com.diy.software.test.iteration1;

import com.diy.hardware.external.CardIssuer;
import com.diy.software.payment.CreditPayment;
import com.jimmyselectronics.opeechee.*;
import com.jimmyselectronics.opeechee.Card.CardData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreditPaymentTest {
    Card testcard = new Card("Visa", "0000000000000000", "Sam", "763", "1234", true, true);
    Card nochipcard = new Card("Visa", "0000000000000001", "Lucinda", "333", "5555", false, false);
    CardData data;
    CardIssuer bank;
    long holdnum;
    CardReader testreader = new CardReader();
    String pin = "1234";
    Calendar expiry = new GregorianCalendar(2024, 9, 31); //Creates an expiry date to pass into the account creater.


    boolean flag;

    @Before
    public void setUp() throws Exception {
        bank = new CardIssuer("ATB", 5000);
        data = testcard.insert("1234");
        bank.addCardData(data.getNumber(), data.getCardholder(), expiry, data.getCVV(), 2000.00); //This "creates an account" for the card with $2000 available credit.
        bank.addCardData("0000000000000001", "Lucinda", expiry, "333", 2000);
        testreader.plugIn();
        testreader.turnOn();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void powerUpTesting() {
        Assert.assertTrue(testreader.isPluggedIn()); // is the reader plugged in? should be yes
        Assert.assertTrue(testreader.isPoweredUp()); // is the reader turned on? should be yes
        testreader.turnOff();
        Assert.assertFalse(testreader.isPoweredUp()); // is the reader turned on?
    }

//	@Test
//	public void cardInsertedTest1() throws IOException {
//		testreader.turnOn();
//		testreader.enable();
//		testreader.insert(testcard, pin);
//	}

    @Test
    public void ConstructorTesting1() throws IOException {
        CreditPayment newpay2 = new CreditPayment(100, testcard, testreader, "1234", bank);
        Assert.assertNotNull(newpay2);
    }

    @Test
    public void ConstructorTesting2() throws IOException {
        new CreditPayment(100, null, testreader, pin, bank);
    }

    @Test  // testing to make sure we return false
    public void ConstructorTesting3() throws IOException {
        CardData data = null;
        CreditPayment newPay = new CreditPayment(100, data, bank);
        flag = newPay.payForTotal();
    }

    @Test  //testing to make sure payfortotal works
    public void ConstructorTesting4() throws IOException {
        CardData data = testcard.insert("1234");
        CreditPayment newPay = new CreditPayment(100, data, bank);
        flag = newPay.payForTotal();
    }

    @Test
    public void InsufficientFunds() {
        //This test will check that payForTotal() correctly returns false if the customer tries to make a payment larger than their account balance
        CreditPayment newpay = new CreditPayment();
        newpay.setCardData(data);
        newpay.setCardIssuer(bank);
        flag = newpay.payForTotal(2001);
        assertFalse(flag);
    }

    @Test
    public void CorrectInsert() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setCard(testcard);
        newpay.setReader(testreader);
        newpay.setCardIssuer(bank);
        newpay.setTotal(300);
        newpay.insertCard("1234");
        flag = newpay.payForTotal();
        assertTrue(flag);
    }

    @Test
    public void ThreeFalsePINS() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setCard(testcard);
        newpay.setReader(testreader);
        newpay.setCardIssuer(bank);
        newpay.setTotal(300);
        flag = true;
        try {
            newpay.insertCard("3333");
        } catch (InvalidPINException e) {
            flag = false;
        }
        assertFalse(flag);
        flag = true;
        try {
            newpay.insertCard("2222");
        } catch (InvalidPINException e) {
            flag = false;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        assertFalse(flag);
        flag = true;
        try {
            newpay.insertCard("1111");
        } catch (InvalidPINException e) {
            flag = false;
        }
        assertFalse(flag);
        try {
            newpay.insertCard("1234"); //Even though the PIN is correct here, it should still fail because the card should be blocked
        } catch (BlockedCardException e) {
            flag = true;
        } catch (Exception e) {
            System.out.println("Some exception encountered other than blocked card." + e.toString());
        }
        assertTrue(flag);
    }

    @Test
    public void NoChipPayment() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setCard(nochipcard);
        newpay.setReader(testreader);
        newpay.setTotal(300);
        newpay.insertCard("5555"); //Even though this is the correct payment, the insert should fail since the card has no chip.
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void PayWithUndefinedObject() {
        CreditPayment newpay = new CreditPayment();
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void NegativeTotal() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setCard(testcard);
        newpay.setReader(testreader);
        newpay.setTotal(300);
        newpay.insertCard("1234");
        newpay.setTotal(-1); //Even though the correct pin was provided, payForTotal() will fail because negative totals aren't allowed
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void ConstructorWithNoChip() throws IOException {
        CreditPayment newpay = new CreditPayment(100, nochipcard, testreader, "5555", bank); //Construction will fail because the card has no chip
        flag = newpay.payForTotal();
        assertFalse(flag);
    }

    @Test
    public void AlternateInsert() throws ChipFailureException, IOException {
        CreditPayment newpay = new CreditPayment();
        newpay.setReader(testreader);
        newpay.setCardIssuer(bank);
        newpay.insertCard(testcard, "1234");
        flag = newpay.payForTotal(300);
        assertTrue(flag);
    }

    // for chip failure exception, creditpayment, force a chipfailure exception?
    // might not need to deal with this, this might be on the hardware side or need a % chance of failure

}
>>>>>>> main
