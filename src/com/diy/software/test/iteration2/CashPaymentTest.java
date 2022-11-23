package com.diy.software.test.iteration2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.Math;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;

import com.diy.hardware.DoItYourselfStationAR;
import com.unitedbankingservices.TooMuchCashException;
import com.unitedbankingservices.DisabledException;
import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.banknote.BanknoteSlotR;
import com.unitedbankingservices.banknote.BanknoteSlotRObserver;
import com.unitedbankingservices.banknote.BanknoteStorageUnit;
import com.unitedbankingservices.banknote.BanknoteValidator;
import com.unitedbankingservices.banknote.BanknoteValidatorObserver;
import com.unitedbankingservices.banknote.IBanknoteDispenser;
import com.unitedbankingservices.banknote.BanknoteStorageUnitObserver;
import com.unitedbankingservices.banknote.BanknoteDispenserObserver;
import com.unitedbankingservices.OutOfCashException;
import com.unitedbankingservices.coin.*;
import com.diy.software.payment.Payment;

import ca.powerutility.NoPowerException;

public class CashPaymentTest {
	
	private static class NoteDispenserObserver implements BanknoteDispenserObserver
	{
		String name;
		
		public NoteDispenserObserver(String newname)
		{
			name = newname;
		}
		
		@Override
		public void moneyFull(IBanknoteDispenser dispenser)
		{
			System.out.println(name + ": Dispenser full.");
		}

		@Override
		public void banknotesEmpty(IBanknoteDispenser dispenser)
		{
			System.out.println(name + ": Dispenser empty.");
		}

		@Override
		public void billAdded(IBanknoteDispenser dispenser, Banknote banknote)
		{
			System.out.println(name + ": " + Long.toString(banknote.getValue()) + " dollar bill added");
		}

		@Override
		public void banknoteRemoved(IBanknoteDispenser dispenser, Banknote banknote)
		{
			System.out.println(name + ": " + Long.toString(banknote.getValue()) + " dollar bill removed."); 
		}

		@Override
		public void banknotesLoaded(IBanknoteDispenser dispenser, Banknote... banknotes)
		{
			System.out.println(name + ": banknotes loaded.");
		}

		@Override
		public void banknotesUnloaded(IBanknoteDispenser dispenser, Banknote... banknotes)
		{
			System.out.println(name + ": banknotes unloaded");
		}
	}
	
	private static class NoteStorageObserver implements BanknoteStorageUnitObserver
	{
		String name;
		
		public NoteStorageObserver(String newname)
		{
			name = newname;
		}
		
		@Override
		public void banknotesFull(BanknoteStorageUnit unit)
		{
			System.out.println(name + ": Banknote storage unit full.");
		}

		@Override
		public void banknoteAdded(BanknoteStorageUnit unit)
		{
			System.out.println(name + ": banknote added to storage. Current count == " + Integer.toString(unit.getBanknoteCount()));
		}

		@Override
		public void banknotesLoaded(BanknoteStorageUnit unit)
		{
			System.out.println(name + ": banknotes loaded. Current count == " + Integer.toString(unit.getBanknoteCount()));
		}

		@Override
		public void banknotesUnloaded(BanknoteStorageUnit unit)
		{
			System.out.println(name + ": banknotes unloaded. Current count == " + Integer.toString(unit.getBanknoteCount()));
		}
	}
	
	private static class NoteValidObserver implements BanknoteValidatorObserver
	{
		String name;
		public NoteValidObserver(String newname)
		{
			name = newname;
		}
		
		@Override
		public void validBanknoteDetected(BanknoteValidator validator, Currency currency, long value)
		{
			System.out.println(name + ": Valid " + currency.getDisplayName() + " bill detected. Value == " + Long.toString(value));
		}
		
		@Override
		public void invalidBanknoteDetected(BanknoteValidator validator)
		{
			System.out.println(name + ": Invalid bill detected.");
		}
	}
	
	private static class MySlotObserver implements BanknoteSlotRObserver
	{    
		String name;
		
		public MySlotObserver(String newname)
		{
			name = newname;
		}
		
		@Override
		public void banknoteInserted(BanknoteSlotR slot)
		{
			System.out.println(name + ": Banknote inserted.");
		}

		/**
		 * An event announcing that a banknote has been returned to the user, dangling
		 * from the slot.
		 * 
		 * @param slot
		 *            The device on which the event occurred.
		 */
		@Override
		public void banknoteEjected(BanknoteSlotR slot)
		{
			System.out.println(name + ": Banknote ejected.");
		}

		/**
		 * An event announcing that a dangling banknote has been removed by the user.
		 * 
		 * @param slot
		 *            The device on which the event occurred.
		 */
		@Override
		public void banknoteRemoved(BanknoteSlotR slot)
		{
			System.out.println(name + ": Banknote removed from slot.");
		}
	}
	
	MySlotObserver observer = new MySlotObserver("Observer");
	MySlotObserver output1 = new MySlotObserver("Output1");
	NoteValidObserver noteobs1 = new NoteValidObserver("Validator1");
	NoteStorageObserver storeobs1 = new NoteStorageObserver("Storage1");
	NoteDispenserObserver dispobs1 = new NoteDispenserObserver("Dispenser1");
	DoItYourselfStationAR station;
	
	Banknote twentybill, tenbill, fivebill, fiftybill, hundredbill;
	Coin penny, nickel, dime, quarter, loonie, toonie; //For the sake of simplicity, we'll assume this test takes place in an alternate universe where canadian pennies are still in circulation
	boolean flag;
	int [] denominations;
	long [] coindenoms;
	
	@Before
	public void setUp() throws Exception {
		denominations = new int[] {5, 10, 20, 50, 100};
		coindenoms = new long[] {1, 5, 10, 25, 100, 200};
		DoItYourselfStationAR.configureBanknoteDenominations(denominations); //Note that the static call to configure banknote denominations
		//is done BEFORE constructing a station
		DoItYourselfStationAR.configureBanknoteStorageUnitCapacity(4); //Only four bills can fit in station2's storage unit
		DoItYourselfStationAR.configureCoinDenominations(coindenoms);
		DoItYourselfStationAR.configureCoinTrayCapacity(10);
		station = new DoItYourselfStationAR();
		station.plugIn();
		station.turnOn();
		
		twentybill = new Banknote(Currency.getInstance("CAD"), 20);
		tenbill = new Banknote(Currency.getInstance("CAD"), 10);
		fivebill = new Banknote(Currency.getInstance("CAD"), 5);
		fiftybill = new Banknote(Currency.getInstance("CAD"), 50);
		hundredbill = new Banknote(Currency.getInstance("CAD"), 100);
		
		penny = new Coin(Currency.getInstance("CAD"), 1);
		nickel = new Coin(Currency.getInstance("CAD"),5);
		dime = new Coin(Currency.getInstance("CAD"),10);
		quarter = new Coin(Currency.getInstance("CAD"),25);
		loonie = new Coin(Currency.getInstance("CAD"),100);
		toonie = new Coin(Currency.getInstance("CAD"),200);
		
		station.banknoteInput.attach(observer);
		station.banknoteValidator.attach(noteobs1);
		station.banknoteStorage.attach(storeobs1);;
		station.banknoteOutput.attach(output1);
		
		station.banknoteDispensers.get(5).load(fivebill, fivebill, fivebill);
		station.banknoteDispensers.get(10).load(tenbill, tenbill);
		station.banknoteDispensers.get(20).load(twentybill, twentybill);
		
		station.coinDispensers.get((long)1).load(penny);
		station.coinDispensers.get((long)5).load(nickel);
		station.coinDispensers.get((long)10).load(dime);
		station.coinDispensers.get((long)25).load(quarter);
		station.coinDispensers.get((long)100).load(loonie);
		station.coinDispensers.get((long)200).load(toonie);
		
		for (int denomination : denominations)
		{
			station.banknoteDispensers.get(denomination).attach(dispobs1);
		}
		//for (long denom: coindenoms)
		//{
			//station.coinDispensers,get(denom).attach(); //Have to make coindispenser obvserver first -_-
		//}
	}
	
	@After
	public void teardown()
	{
		for (int notedenom : denominations)
		{
			station.banknoteDispensers.get(notedenom).unload();
		}
		for (long coindenom : coindenoms)
		{
			station.coinDispensers.get(coindenom).unload();
		}
	}
	
	@Test
	public void NormalPayment() throws DisabledException, TooMuchCashException
	{
		Payment newpay = new Payment(station, 19.75);
		assertTrue(newpay.checkoutTotal == 19.75);
		station.banknoteInput.receive(twentybill);
		assertTrue(newpay.checkoutTotal == 0);
		List<Coin> coinlist = station.coinTray.collectCoins();
		assertTrue(coinlist.size() == 1); //The station should have given one quarter as change.
		assertTrue(coinlist.get(0).getValue() == 25);
	}
	
	@Test
	public void NotEnoughChange() throws DisabledException, TooMuchCashException
	{
		Payment newpay = new Payment(station, 19.50);
		station.banknoteInput.receive(twentybill); //There is only one quarter in the dispenser, so the customer won't receive enough change
		assertTrue(newpay.checkoutTotal == -0.25);
		List<Coin> coinlist = station.coinTray.collectCoins();
		assertTrue(coinlist.size() == 1);
		assertTrue(coinlist.get(0).getValue() == 25);
	}
	
	@Test
	public void PartialPayment() throws DisabledException, TooMuchCashException
	{
		Payment newpay = new Payment(station, 40);
		station.banknoteInput.receive(twentybill); //There is only one quarter in the dispenser, so the customer won't receive enough change
		assertTrue(newpay.checkoutTotal == 20);
		List<Coin> coinlist = station.coinTray.collectCoins();
		assertTrue(coinlist.size() == 0);
	}
	
	@Test
	public void SameBillTwice() throws DisabledException, TooMuchCashException
	{
		Payment newpay = new Payment(station, 40);
		station.banknoteInput.receive(twentybill); //There is only one quarter in the dispenser, so the customer won't receive enough change
		station.banknoteInput.receive(twentybill);
		assertTrue(newpay.checkoutTotal == 0);
	}
	
	@Test
	public void BillsInChange() throws DisabledException, TooMuchCashException
	{
		Payment newpay = new Payment(station, 3.75);
		station.banknoteInput.receive(tenbill); //There is only one quarter in the dispenser, so the customer won't receive enough change
		System.out.println("After inserting ten dollar bill, checkout total is " + Double.toString(newpay.checkoutTotal));
		assertTrue(newpay.checkoutTotal == 0);
		List<Coin> coinlist = station.coinTray.collectCoins();
		assertTrue(coinlist.size() == 2);
	}
	
	@Test
	public void LargeBillSmallCheckout() throws DisabledException, TooMuchCashException
	{
		Payment newpay = new Payment(station, 3.75);
		station.banknoteInput.receive(hundredbill); //There is only one quarter in the dispenser, so the customer won't receive enough change
		List<Coin> coinlist = station.coinTray.collectCoins();
		//The system should have been able to give a loonie, and a quarter as change, but then couldn't find any fifty dollar bills and gave up
		assertTrue(coinlist.size() == 2);
		assertTrue(newpay.checkoutTotal == (-95));
	}
	
	
}