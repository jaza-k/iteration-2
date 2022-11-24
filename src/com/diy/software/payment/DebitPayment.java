package com.diy.software.payment;

import java.io.IOException;
import ca.powerutility.NoPowerException;
import com.diy.hardware.external.CardIssuer;
import com.jimmyselectronics.opeechee.Card.CardData;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.InvalidPINException;
import com.jimmyselectronics.opeechee.CardReader;
import com.jimmyselectronics.opeechee.ChipFailureException;
import com.jimmyselectronics.opeechee.BlockedCardException;

public class DebitPayment {
	
	private double total;
	private CardData data;
	private CardIssuer bank;
	private Card card;
	private CardReader reader;
	
	private CardData cardReaderInterface(String pin) throws NoPowerException, ChipFailureException, IOException, IllegalStateException, InvalidPINException, BlockedCardException {
		// check if card and reader are null
        if (card == null || reader == null) return null; 
		
        CardData cardDataReturn;
		try {
            // insert card into reader
			cardDataReturn = this.reader.insert(card, pin);
			removeCard();
            // return card data from the reader
			return cardDataReturn;
		}
        // if chip fails
		catch (ChipFailureException e) {
			removeCard();
			return null;
		}
        // if pin is invalid
		catch (InvalidPINException e) {
			removeCard();
			throw new InvalidPINException();
		} 
        // if card is blocked
        catch (BlockedCardException e)
		{
			removeCard();
			throw new BlockedCardException();
		}
	}
	
    /**
     * Basic constructor for DebitPayment
     */
	public DebitPayment() {
		this.total = 0;
		this.card = null;
		this.reader = null;
		this.bank = null;
		this.data = null;
	}
	
    /** 
     * Method to set this.card to newCard
     * @param newCard the card to set
    */
	public void setCard(Card newCard) {
		this.card = newCard;
	}
	
    /**
     * This version of insertCard() inserts card which has already been set
     * @param pin the pin to use
     * @throws ChipFailureException
     * @throws IOException
     * @throws InvalidPINException
     */
	public void insertCard(String pin) throws ChipFailureException, IOException, InvalidPINException {
        try {
            // get data from inserting into reader
			this.data = cardReaderInterface(pin);
		}
		catch (InvalidPINException e) {
			throw new InvalidPINException();
		}
		catch (BlockedCardException e) {
			throw new BlockedCardException();
		}
	}

    /**
     * Overridden version of insertCard() inserts card which is passed in as parameter
     * @param newCard
     * @param pin
     * @throws ChipFailureException
     * @throws IOException
     * @throws InvalidPINException
     * @throws BlockedCardException
     */
	public void insertCard(Card newCard, String pin) throws ChipFailureException, IOException, InvalidPINException, BlockedCardException {
		this.card = newCard;
		try {
			insertCard(pin);
		}
		catch (InvalidPINException e) {
			throw new InvalidPINException();
		}
		catch (BlockedCardException e) {
			throw new BlockedCardException();
		}
	}
	
    /**
     * Method to remove card from reader
     */
	public void removeCard() {
		this.reader.remove();
	}
	
    /**
     * Method to set this.reader to newReader
     * @param newReader the new reader to set
     */
	public void setReader(CardReader newReader) {
		this.reader = newReader;
	}

    /**
     * Method to set this.total to newTotal
     * @param newTotal the new total to set
     */
	public void setTotal(double newTotal) {
		total = newTotal;
	}

    /**
     * Method to set this.data to newData
     * @param newData the new card data to set
     */
	public void setCardData(CardData newData) {
		data = newData;
	}

    /**
     * Method to set this.bank to newBank
     * @param newBank the new bank/card issuer to set
     */
	public void setCardIssuer(CardIssuer newBank) {
		bank = newBank;
	}
	
    /**
     * Alternate constructor for DebitPayment which sets all fields, allowing for hardware interface
     * @param newTotal the total passed in
     * @param newCard the card passed in
     * @param newReader the card reader passed in
     * @param newPin the pin passed in
     * @param newBank the bank passed in
     * @throws ChipFailureException
     * @throws IOException
     * @throws InvalidPINException
     * @throws BlockedCardException
     */
	public DebitPayment(double newTotal, Card newCard, CardReader newReader, String newPin, CardIssuer newBank) throws ChipFailureException, IOException, InvalidPINException, BlockedCardException {
		if (newCard.kind.equalsIgnoreCase("credit")) {
            throw new IllegalArgumentException("Cannot use credit card for debit payment");
        }
        this.total = newTotal;
		this.card = newCard;
		this.reader = newReader;
		this.bank = newBank;
		try {
			this.data = cardReaderInterface(newPin);
		}
		catch (InvalidPINException e) {
			throw new InvalidPINException();
		}
		catch (BlockedCardException e) {
			throw new BlockedCardException();
		}
		catch (Exception e) {
			this.total = 0;
			this.card = null;
			this.reader = null;
			this.bank = null;
			this.data = null;
		}
	}
	
    /** Alternate constructor for DebitPayment, where CardData should have already been generated
     * and the card and reader values are kept null
     * @param newTotal the total passed in
     * @param newCard the card passed in
     * @param newBank the bank passed in
     * 
     */
	public DebitPayment(double newTotal, CardData newData, CardIssuer newBank) {
		total = newTotal;
		data = newData;
		bank = newBank;
	}
	
    /**
     * Method to process payment and verify transaction
     * Attempts to use CardData (presumably generated by inserting a card into a reader) to pay at checkout
     * @return true if payment is successful, false otherwise
     */
	public boolean payForTotal() {
		boolean flag;
        
		if (data == null || bank == null || total < 0) return false;

		long holdNum; // ID value for a hold at the bank
		holdNum = bank.authorizeHold(data.getNumber(), total); // attempts to put a hold on the specified amount

        // authorizeHold() returns -1 on any failure, so if holdNum is any other value, the bank successfully put a hold on the funds
		if (holdNum != -1)  {
			flag = bank.postTransaction(data.getNumber(), holdNum, total); // Remove funds from the customer's account, set response to flag
			bank.releaseHold(data.getNumber(), holdNum); // Eliminate the hold corresponding to holdNum
			return flag;
		}
		return false;
	}
	
    /**
     * Overridden version of payForTotal() which will receive a custom total amount 
     * before calling the original payForTotal() method
     * @param newTotal the new total to set
     * @return true if payment is successful, false otherwise
     */
    //overloaded version of payForTotal will set total to newtotal before calling payForTotal()
	public boolean payForTotal(double newTotal) {
		this.total = newTotal;
		return payForTotal();
	}
}