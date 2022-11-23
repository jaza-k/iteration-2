package com.diy.software.payment;

import java.util.Currency;

import com.diy.hardware.DoItYourselfStationAR;
import com.unitedbankingservices.banknote.*;
import com.unitedbankingservices.coin.*;

public class Payment {
	public static double checkoutTotal;
	public static DoItYourselfStationAR customerStation;
	private static Banknote latestnote;
	private static boolean cashinserted;
	private static boolean coininserted;
	private static Coin latestcoin;
	
	private static class CoinStorage implements CoinStorageUnitObserver
	{
		@Override
		public void coinsFull(CoinStorageUnit unit) {}

		@Override
		public void coinAdded(CoinStorageUnit unit)
		{
			if (coininserted && latestcoin != null)
			{
				try
				{
					CashPayment newpayment = new CashPayment(customerStation);
					checkoutTotal = newpayment.payWithCoin(latestcoin, checkoutTotal);
					coininserted = false;
					latestcoin = null;
				}
				catch (Exception e)
				{
					//alert attendant
				}
			}
		}

		@Override
		public void coinsLoaded(CoinStorageUnit unit) {}

		@Override
		public void coinsUnloaded(CoinStorageUnit unit) {}
	}
	
	private static class CoinValid implements CoinValidatorObserver
	{
		@Override
		public void validCoinDetected(CoinValidator validator, long value)
		{
			latestcoin = new Coin(value);
			coininserted = true;
		}

		@Override
		public void invalidCoinDetected(CoinValidator validator) {}
	}
	
	private static class NoteStorage implements BanknoteStorageUnitObserver
	{
		public NoteStorage() {}
		@Override
		public void banknotesFull(BanknoteStorageUnit unit) {}

		@Override
		public void banknoteAdded(BanknoteStorageUnit unit) 
		{
			if (cashinserted && latestnote != null)
			{
				//If a banknote was added to the storage, then we should do a cash payment event
				try
				{
					CashPayment newpayment = new CashPayment(customerStation);
					checkoutTotal = newpayment.payWithBill(latestnote, checkoutTotal);
					cashinserted = false;
					latestnote = null;
				}
				catch (Exception e)
				{
					//Alert attendant. 
				}
				//We would want to alert an attendant here if checkoutTotal < 0, because that would mean the customer is still owed change and there wasn't enough in the dispenser
			}
		}

		@Override
		public void banknotesLoaded(BanknoteStorageUnit unit)
		{
			//This could re-enable the station if it was deactivated while the attendant unloaded the cash storage.
		}

		@Override
		public void banknotesUnloaded(BanknoteStorageUnit unit) {} //The station should already be disabled if an attendant unloads it, so i don't think we'd have to do anything here
	}
	
	private static class NoteValid implements BanknoteValidatorObserver
	{
		public NoteValid() {}
		
		@Override
		public void validBanknoteDetected(BanknoteValidator validator, Currency currency, long value)
		{
			latestnote = new Banknote(currency, value);
			cashinserted = true;
		}

		@Override
		public void invalidBanknoteDetected(BanknoteValidator validator)
		{
			//In this case, it may be a good idea to alert the attendant.
		}
	}
	
	private NoteValid notevalidob; //These observers should handle the insertion of banknotes
	private NoteStorage notestoreob;
	
	private CoinValid coinvalidob;
	private CoinStorage coinstoreob; //These observers will hand the insertion of coins
	
	
	
	public Payment(DoItYourselfStationAR newstation, double newtotal)
	{
		customerStation = newstation;
		checkoutTotal = newtotal;
		latestnote = null;
		latestcoin = null;
		coininserted = false;
		cashinserted = false;
		notevalidob = new NoteValid();
		notestoreob = new NoteStorage();
		coinvalidob = new CoinValid();
		coinstoreob = new CoinStorage();
		
		customerStation.banknoteValidator.attach(notevalidob); //By attaching the observers to the station, the software can detect when
		customerStation.banknoteStorage.attach(notestoreob); //bills or coins are inserted into the checkout station. it may be a good
		customerStation.coinValidator.attach(coinvalidob); //idea to attach them only in the case when the customer chooses to pay with
		customerStation.coinStorage.attach(coinstoreob);  //cash at the checkout menu on their gui.
	}
}
