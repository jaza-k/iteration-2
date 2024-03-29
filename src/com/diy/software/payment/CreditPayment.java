package com.diy.software.payment;

import ca.powerutility.NoPowerException;
import com.diy.hardware.external.CardIssuer;
import com.jimmyselectronics.opeechee.*;
import com.jimmyselectronics.opeechee.Card.CardData;

import java.io.IOException;

public class CreditPayment {

    private double total;
    private CardData data;
    private CardIssuer bank;
    private Card card;
    private CardReader reader;

    private CardData cardReaderInterface(String pin) throws NoPowerException, ChipFailureException, IOException, IllegalStateException, InvalidPINException, BlockedCardException {
        //This function will be called if a CreditPayment object is constructed with a reader and a card instead of just card data
        //It's assumed that the "card" and "reader" properties have already been defined before the function is called.
        if (card == null || reader == null)
            return null; //If this check fails, then both card and reader have some non-null value
        CardData returner;
        try {
            returner = reader.insert(card, pin);
            removeCard();
            return returner;
        } catch (ChipFailureException e) {
            removeCard();
            return null;
        } catch (InvalidPINException e) {
            removeCard();
            throw new InvalidPINException();
        } catch (BlockedCardException e) {
            removeCard();
            throw new BlockedCardException();
        }

    }

    public CreditPayment() {
        total = 0;
        card = null;
        reader = null;
        bank = null;
        data = null;
    }

    public void setCard(Card newcard) {
        card = newcard;
    }

    public void insertCard(String pin) throws ChipFailureException, IOException, InvalidPINException {
        //This version of insertCard will insert the card that's already set as the card property, with some PIN
        try {
            data = cardReaderInterface(pin);
        } catch (InvalidPINException e) {
            throw new InvalidPINException();
        } catch (BlockedCardException e) {
            throw new BlockedCardException();
        }
    }

    public void insertCard(Card newcard, String pin) throws ChipFailureException, IOException, InvalidPINException, BlockedCardException {
        //Overloaded version of insertCard will change the card property
        card = newcard;
        try {
            insertCard(pin);
        } catch (InvalidPINException e) {
            throw new InvalidPINException();
        } catch (BlockedCardException e) {
            throw new BlockedCardException();
        }
    }

    public void removeCard() {
        reader.remove();
        //card = null;
    }

    public void setReader(CardReader newreader) {
        reader = newreader;
    }

    public void setTotal(double newtotal) {
        total = newtotal;
    }

    public void setCardData(CardData newdata) {
        data = newdata;
    }

    public void setCardIssuer(CardIssuer newbank) {
        bank = newbank;
    }

    public CreditPayment(double newtotal, Card newcard, CardReader newreader, String newpin, CardIssuer newbank) throws ChipFailureException, IOException, InvalidPINException, BlockedCardException {
        //This class will have alternate constructors. This one is passed a card reader, a card, and a pin, allowing for hardware interface
        total = newtotal;
        card = newcard;
        reader = newreader;
        bank = newbank;
        try {
            data = cardReaderInterface(newpin);
        } catch (InvalidPINException e) {
            throw new InvalidPINException();
        } catch (BlockedCardException e) {
            throw new BlockedCardException();
        } catch (Exception e) {
            total = 0;
            card = null;
            reader = null;
            bank = null;
            data = null;
        }
    }

    public CreditPayment(double newtotal, CardData newdata, CardIssuer newbank) {
        //For this alternate constructor, the CardData should have already been generated, and we can keep the card and reader properties set to null.
        total = newtotal;
        data = newdata;
        bank = newbank;
    }

    public boolean payForTotal() {
        //This function will attempt to use CardData (presumably generated by inserting a card into a reader) to pay at checkout
        boolean flag;
        if (data == null || bank == null || total < 0)
            return false; //may be better to throw exceptions than return false, idk
        long holdnum; //ID value for a hold at the bank
        holdnum = bank.authorizeHold(data.getNumber(), total); //We'll attempt to put a hold on the specified amount.
        if (holdnum != -1) //authorizeHold returns -1 on any failure, so if holdnum is any other value, the bank successfully put a hold on the funds
        {
            flag = bank.postTransaction(data.getNumber(), holdnum, total); //This should remove the funds from the customer's account.
            bank.releaseHold(data.getNumber(), holdnum);
            return flag;
        }
        return false;
    }

    public boolean payForTotal(double newtotal) {
        //overloaded version of payForTotal will set total to newtotal before calling payForTotal()
        total = newtotal;
        return payForTotal();
    }
}