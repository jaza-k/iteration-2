package com.diy.software.payment;

import java.util.Currency;

import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.jimmyselectronics.opeechee.Card;
import com.diy.software.AttendantStationLogic;
import com.diy.software.DoItYourselfStationLogic;
import com.unitedbankingservices.TooMuchCashException;
import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.banknote.BanknoteStorageUnit;
import com.unitedbankingservices.banknote.BanknoteStorageUnitObserver;
import com.unitedbankingservices.banknote.BanknoteValidator;
import com.unitedbankingservices.banknote.BanknoteValidatorObserver;
import com.unitedbankingservices.coin.Coin;
import com.unitedbankingservices.coin.CoinStorageUnit;
import com.unitedbankingservices.coin.CoinStorageUnitObserver;
import com.unitedbankingservices.coin.CoinValidator;
import com.unitedbankingservices.coin.CoinValidatorObserver;
import com.unitedbankingservices.DisabledException;

public class Payment {
	public static double checkoutTotal;
	public static DoItYourselfStationAR customerStation;
	public static DoItYourselfStationLogic stationLogic;
	private static Banknote latestnote;
	private static boolean cashinserted;
	private static boolean coininserted;
	private static Coin latestcoin;

	private static class CoinStorage implements CoinStorageUnitObserver
	{
		@Override
		public void coinsFull(CoinStorageUnit unit)
		{
			stationLogic.block(customerStation);
			int stationid = AttendantStationLogic.getInstance().matchStationID(stationLogic);
			AttendantStationLogic.getInstance().notifyProblem(stationid, 4);
		}

		@Override
		public void coinAdded(CoinStorageUnit unit)
		{
			if (coininserted = true && latestcoin != null) {
				try {
					CashPayment newpayment = new CashPayment(customerStation, stationLogic);
					checkoutTotal = newpayment.payWithCoin(latestcoin, checkoutTotal);
					coininserted = false;
					latestcoin = null;
				} catch (TooMuchCashException e) {
					stationLogic.block(customerStation);
					int stationid = AttendantStationLogic.getInstance().matchStationID(stationLogic);
					AttendantStationLogic.getInstance().notifyProblem(stationid, 4);
				} catch (Exception e) {
				}
			}
		}

		@Override
		public void coinsLoaded(CoinStorageUnit unit){}

		@Override
		public void coinsUnloaded(CoinStorageUnit unit) {}
	}

	private static class CoinValid implements CoinValidatorObserver
	{
		@Override
		public void validCoinDetected(CoinValidator validator, long value)
		{
			//System.out.println("Valid coin detected");
			latestcoin = new Coin(Currency.getInstance("CAD"), value);
			coininserted = true;
			try
			{
				customerStation.coinStorage.receive(latestcoin);
			}
			catch (TooMuchCashException e)
			{
				stationLogic.block(customerStation);
				int stationid = AttendantStationLogic.getInstance().matchStationID(stationLogic);
				AttendantStationLogic.getInstance().notifyProblem(stationid, 4);
			}
			catch (Exception e) {}
		}

		@Override
		public void invalidCoinDetected(CoinValidator validator) {}
	}

	private static class NoteStorage implements BanknoteStorageUnitObserver
	{
		public NoteStorage() {}
		@Override
		public void banknotesFull(BanknoteStorageUnit unit)
		{
			stationLogic.block(customerStation);
			int stationid = AttendantStationLogic.getInstance().matchStationID(stationLogic);
			AttendantStationLogic.getInstance().notifyProblem(stationid, 4);
		}

		@Override
		public void banknoteAdded(BanknoteStorageUnit unit)
		{
			if (cashinserted && latestnote != null)
			{
				//If a banknote was added to the storage, then we should do a cash payment event
				try
				{
					CashPayment newpayment = new CashPayment(customerStation, stationLogic);
					checkoutTotal = newpayment.payWithBill(latestnote, checkoutTotal);
					cashinserted = false;
					latestnote = null;
				}
				catch (TooMuchCashException e)
				{
					stationLogic.block(customerStation);
					int stationid = AttendantStationLogic.getInstance().matchStationID(stationLogic);
					AttendantStationLogic.getInstance().notifyProblem(stationid, 4);
				}
				catch (Exception e) {}
				//We would want to alert an attendant here if checkoutTotal < 0, because that would mean the customer is still owed change and there wasn't enough in the dispenser
				if (checkoutTotal < 0)
				{
					stationLogic.block(customerStation);
					int stationid = AttendantStationLogic.getInstance().matchStationID(stationLogic);
					AttendantStationLogic.getInstance().notifyProblem(stationid, 3);
				}
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

	public boolean CreditPay(String newpin, Card newcard, double payval, CardIssuer newbank) //I apologize for how many parameters go into this call.
	{
		//Call this function when the customer chooses to pay with a credit card. Using payval allows for the possibility of partial payments.
		try
		{
			CreditPayment newpay = new CreditPayment(payval, newcard, customerStation.cardReader, newpin, newbank);
			if (newpay.payForTotal())
			{
				checkoutTotal -= payval; //Subtract amount paid by customer on a successful transaction
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public void dispenseChange() throws DisabledException, TooMuchCashException
	{
		//If the customer is owed change, this function should be called after the attendant refills the change dispensers.
		double returner;
		CashPayment newchange = new CashPayment(customerStation, stationLogic);
		if (checkoutTotal < 0)
		{
			checkoutTotal = newchange.dispenseChange(-checkoutTotal);
		}
	}

	public Payment(DoItYourselfStationAR newstation, DoItYourselfStationLogic newlogic, double newtotal)
	{
		customerStation = newstation;
		stationLogic = newlogic;
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
