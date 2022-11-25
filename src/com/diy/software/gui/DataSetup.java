<<<<<<< HEAD
package com.diy.software.gui;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.Card.CardData;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DataSetup {
    private final String[] ITEM_LABELS = {"Aveeno Lotion", "Ritz Crackers", "Coca-Cola", "Pepsi", "Quakers Oats", "Frootloops", "Orignal Lays Chips"};
    private Customer customer;
    private CardIssuer bank;

    public DataSetup(Customer customer) {
        this.customer = customer;
        // Populating our item data base (for the store)
        populateDataBase();

        // Setup of customer's information (banking, cards, cart and its items)
        Card customerCard = new Card("Visa", "0000000000000000", "Sam", "763", "1234", true, true);
        customer.wallet.cards.add(customerCard);
        // Updating information for the Card in customer's wallet
        bank = new CardIssuer("ATB", 5000);
        String pin = "1234";
        Calendar expiry = new GregorianCalendar(2024, 9, 31);
        CardData data = null;
        // Want to be able to always do this, without an exception (as its vital to our payment)
        try {
            data = customerCard.insert("1234");
            System.out.println("successful");
            System.out.println(data.getKind());

        } catch (IOException e) {
            System.out.println("Somehow emperor palpatine has returned..");

        }
        bank.addCardData(data.getNumber(), data.getCardholder(), expiry, data.getCVV(), 2000.00);

        initialCart();
    }

    /**
     * Creates the initial cart with all the items that are yet to be scanned
     */
    private void initialCart() {
        Barcode[] barcodes = new Barcode[4];
        for (int i = 0; i < 4; i++) {
            barcodes[i] = new Barcode(new Numeral[]{Numeral.valueOf((byte) i)});
            BarcodedItem item = new BarcodedItem(barcodes[i], (long) (1.0 + i));
            customer.shoppingCart.add(item);
        }
    }

    /**
     * Populates the data base that represents the inventory of the store
     */
    private void populateDataBase() {
        Barcode[] barcodes = new Barcode[7];
        for (int i = 0; i < 7; i++) {
            barcodes[i] = new Barcode(new Numeral[]{Numeral.valueOf((byte) i)});
            BarcodedProduct product = new BarcodedProduct(barcodes[i], ITEM_LABELS[i], (long) (1.0 + i), 5);
            ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodes[i], product);
            ProductDatabases.INVENTORY.put(product, 1);
        }
    }

    public CardIssuer getBank() {
        // TODO Auto-generated method stub
        return bank;
    }
=======
package com.diy.software.gui;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.Card.CardData;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DataSetup {
    private final String[] ITEM_LABELS = {"Aveeno Lotion", "Ritz Crackers", "Coca-Cola", "Pepsi", "Quakers Oats", "Frootloops", "Orignal Lays Chips"};
    private Customer customer;
    private CardIssuer bank;

    public DataSetup(Customer customer) {
        this.customer = customer;
        // Populating our item data base (for the store)
        populateDataBase();

        // Setup of customer's information (banking, cards, cart and its items)
        Card customerCard = new Card("Visa", "0000000000000000", "Sam", "763", "1234", true, true);
        customer.wallet.cards.add(customerCard);
        // Updating information for the Card in customer's wallet
        bank = new CardIssuer("ATB", 5000);
        String pin = "1234";
        Calendar expiry = new GregorianCalendar(2024, 9, 31);
        CardData data = null;
        // Want to be able to always do this, without an exception (as its vital to our payment)
        try {
            data = customerCard.insert("1234");
            System.out.println("successful");
            System.out.println(data.getKind());

        } catch (IOException e) {
            System.out.println("Somehow emperor palpatine has returned..");

        }
        bank.addCardData(data.getNumber(), data.getCardholder(), expiry, data.getCVV(), 2000.00);

        initialCart();
    }

    /**
     * Creates the initial cart with all the items that are yet to be scanned
     */
    private void initialCart() {
        Barcode[] barcodes = new Barcode[4];
        for (int i = 0; i < 4; i++) {
            barcodes[i] = new Barcode(new Numeral[]{Numeral.valueOf((byte) i)});
            BarcodedItem item = new BarcodedItem(barcodes[i], (long) (1.0 + i));
            customer.shoppingCart.add(item);
        }
    }

    /**
     * Populates the data base that represents the inventory of the store
     */
    private void populateDataBase() {
        Barcode[] barcodes = new Barcode[7];
        for (int i = 0; i < 7; i++) {
            barcodes[i] = new Barcode(new Numeral[]{Numeral.valueOf((byte) i)});
            BarcodedProduct product = new BarcodedProduct(barcodes[i], ITEM_LABELS[i], (long) (1.0 + i), 5);
            ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodes[i], product);
            ProductDatabases.INVENTORY.put(product, 1);
        }
    }

    public CardIssuer getBank() {
        // TODO Auto-generated method stub
        return bank;
    }
>>>>>>> main
}