package com.diy.software.payment;

import com.diy.hardware.DoItYourselfStationAR;
import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.TooMuchCashException;
import com.unitedbankingservices.OutOfCashException;
import com.unitedbankingservices.DisabledException;
import java.lang.Math;
import java.util.List;
import com.unitedbankingservices.coin.Coin;

public class CashPayment {
	DoItYourselfStationAR customerStation;
	
	public CashPayment(DoItYourselfStationAR newstation)
	{
		customerStation = newstation;
	}
	
	public double payWithBill(Banknote bill, double checkoutTotal) throws TooMuchCashException, DisabledException
	{
		//It is assumed that the bill was already successfully inserted, validated, and stored in the parent class
		double billvalue = bill.getValue();
		if (checkoutTotal - billvalue >= 0) //In this case, no change will be dispensed
		{
			return checkoutTotal - billvalue; //Return remaining value to be paid to whoever called the function.
		}
		else //If the difference is negative, then we should dispense change
		{
			double returner = dispenseChange(-(checkoutTotal - billvalue));
			return returner;
		}
	}
	
	public double payWithCoin(Coin coin, double checkoutTotal) throws TooMuchCashException, DisabledException
	{
		double coinvalue = coin.getValue() / 100;
		if (checkoutTotal - coinvalue >= 0)
		{
			return checkoutTotal - coinvalue;
		}
		else
		{
			double returner = dispenseChange(-(checkoutTotal - coinvalue));
			return returner;
		}
	}
	
	public double dispenseChange(double changevalue) throws TooMuchCashException, DisabledException
	{
		double temp;
		long dollarvalue = (long)Math.floor(changevalue);
		temp = (changevalue - dollarvalue) * 100; //This will be the number of cents we have to give to the customer
		long centvalue = (long)Math.floor(temp);
		centvalue += (dollarvalue % 5) * 100; //Five dollar bills are the smallest denomination of canadian bills. Add remainder to centvalue
		dollarvalue -= (dollarvalue % 5); //Now dollarvalue should be some multiple of 5
		int[] notedenom = customerStation.banknoteDenominations;
		List<Long> coindenom = customerStation.coinDenominations;
		long cointoget = 0;
		int billtoget = 0;
		while (centvalue > 0)
		{
			for (int i = 0; i < coindenom.size(); i++)
			{
				if (cointoget < coindenom.get(i) && coindenom.get(i) <= centvalue) cointoget = coindenom.get(i);
			}
			try
			{
				customerStation.coinDispensers.get(cointoget).emit();
				centvalue -= cointoget; 
				cointoget = 0;
			}
			catch (OutOfCashException e)
			{
				temp = dollarvalue + ((double)centvalue / 100);
				return -temp; //If some negative value
			}
		}
		while (dollarvalue > 0)
		{
			for (int i = 0; i < notedenom.length; i++)
			{
				if (billtoget < notedenom[i] && notedenom[i] <= dollarvalue) billtoget = notedenom[i];
			}
			try
			{
				customerStation.banknoteDispensers.get(billtoget).emit();
				customerStation.banknoteOutput.removeDanglingBanknote();
				dollarvalue -= billtoget;
				billtoget = 0;
			}
			catch (OutOfCashException e)
			{
				temp = dollarvalue; //We don't have to add the centvalue to the returned value here because we already should have dispensed all the cents in the first while loop
				return -temp;
			}
		}
		return 0;
	}
}
