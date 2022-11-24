package com.diy.software.test.iteration2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.diy.hardware.external.CardIssuer;
import com.diy.software.payment.CreditPayment;
import com.diy.software.payment.DebitPayment;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.CardReader;
import com.jimmyselectronics.opeechee.Card.CardData;
import com.jimmyselectronics.opeechee.ChipFailureException;
import com.jimmyselectronics.opeechee.InvalidPINException;
import com.jimmyselectronics.opeechee.BlockedCardException;

public class DebitPaymentTest {
	Card testDebitCard = new Card("Debit", "0000000000000000", "Ben", "567", "1234", true, true);
	Card testNoChipDebitCard = new Card("Debit", "1000000000000001", "Joe", "111", "0000", false, false);
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

	@After
	public void tearDown() throws Exception {
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
	public void ConstructorTesting2() throws IOException {
		new DebitPayment(100, null, testReader, pin, bank);
	}
	
	@Test 
	public void ConstructorTesting3() throws IOException {
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
	public void InsufficientFunds()
	{
		//This test will check that payForTotal() correctly returns false if the customer tries to make a payment larger than their account balance
		DebitPayment newPay = new DebitPayment();
		newPay.setCardData(data);
		newPay.setCardIssuer(bank);
		flag = newPay.payForTotal(2001);
		assertFalse(flag);
	}
	
	@Test
	public void CorrectInsert() throws ChipFailureException, IOException
	{
		DebitPayment newPay = new DebitPayment();
		newPay.setCard(testDebitCard);
		newPay.setReader(testReader);
		newPay.setCardIssuer(bank);
		newPay.setTotal(300);
		newPay.insertCard("1234");
		flag = newPay.payForTotal();
		assertTrue(flag);
	}
	
	@Test
	public void ThreeFalsePINS() throws ChipFailureException, IOException
	{
		DebitPayment newPay = new DebitPayment();
		newPay.setCard(testDebitCard);
		newPay.setReader(testReader);
		newPay.setCardIssuer(bank);
		newPay.setTotal(300);
		flag = true;
		try {
			newPay.insertCard("3333");
		}
		catch (InvalidPINException e) {
			flag = false;
		}
		assertFalse(flag);
		flag = true;
		try {
			newPay.insertCard("2222");
		}
		catch (InvalidPINException e) {
			flag = false;
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		assertFalse(flag);
		flag = true;
		try {
			newPay.insertCard("1111");
		}
		catch (InvalidPINException e) {
			flag = false;
		}
		assertFalse(flag);
		try {
			newPay.insertCard("1234"); //Even though the PIN is correct here, it should still fail because the card should be blocked
		}
		catch (BlockedCardException e) {
			flag = true;
		}
		catch (Exception e) {
			System.out.println("Some exception encountered other than blocked card." + e.toString());
		}
		assertTrue(flag);
	}
	
	@Test
	public void NoChipPayment() throws ChipFailureException, IOException {
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
	public void NegativeTotal() throws ChipFailureException, IOException {
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
	public void AlternateInsert() throws ChipFailureException, IOException {
		DebitPayment newpay = new DebitPayment();
		newpay.setReader(testReader);
		newpay.setCardIssuer(bank);
		newpay.insertCard(testDebitCard, "1234");
		flag = newpay.payForTotal(300);
		assertTrue(flag);
	}
}
