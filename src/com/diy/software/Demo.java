package com.diy.software;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.opeechee.Card;
import com.unitedbankingservices.coin.Coin;

import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;

public class Demo {
    public static void main(String[] args) {

        Coin.DEFAULT_CURRENCY = Currency.getInstance("CAD");

        // Configuring Stations
        int[] denominations = new int[]{5, 10, 20, 50, 100};
        long[] coindenoms = new long[]{1, 5, 10, 25, 100, 200};
        DoItYourselfStationAR.configureBanknoteDenominations(denominations); //Note that the static call to configure banknote denominations
        DoItYourselfStationAR.configureBanknoteStorageUnitCapacity(4); //Only four bills can fit in station2's storage unit
        DoItYourselfStationAR.configureCoinDenominations(coindenoms);
        DoItYourselfStationAR.configureCoinTrayCapacity(10);
        DoItYourselfStationAR.configureCurrency(Currency.getInstance("CAD"));
        DoItYourselfStationAR.configureCoinStorageUnitCapacity(1000);


        // Create station - MOVED BELOW
        //DoItYourselfStationAR station = new DoItYourselfStationAR();

        // Changes above to accept multiple stations as list based on size of store
        int locationSize = 6;    // 6 is arbitrary value representing a hypothetical store size (may limit to 99 later)
        DoItYourselfStationAR[] stsAR = new DoItYourselfStationAR[locationSize];
        for (int i = 0; i < locationSize; i++) {
            DoItYourselfStationAR station = new DoItYourselfStationAR();
            stsAR[i] = station;
        }

        // Create barcodes
        Barcode barcode1 = new Barcode(new Numeral[]{Numeral.one});
        Barcode barcode2 = new Barcode(new Numeral[]{Numeral.two, Numeral.three});
        Barcode barcode3 = new Barcode(new Numeral[]{Numeral.one, Numeral.two, Numeral.three});

        // Create barcoded items
        BarcodedItem item1 = new BarcodedItem(barcode1, 1);
        BarcodedItem item2 = new BarcodedItem(barcode2, 6);
        BarcodedItem item3 = new BarcodedItem(barcode3, 20);

        // Create barcoded products
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "Crackers", 5, 1);
        BarcodedProduct product2 = new BarcodedProduct(barcode2, "Juice", 10, 5);
        BarcodedProduct product3 = new BarcodedProduct(barcode3, "Chocolate", 4, 20);


        // Add barcoded products to database
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode3, product3);

        Barcode bagBarcode = new Barcode(new Numeral[]{Numeral.one, Numeral.two, Numeral.three, Numeral.three, Numeral.nine});
        BarcodedProduct bagBP = new BarcodedProduct(bagBarcode, "Store Purchased Bags", 1, 1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(bagBarcode, bagBP);
        ProductDatabases.INVENTORY.put(bagBP, 1000);

        BarcodedItem bagItem = new BarcodedItem(bagBarcode, 1);


        // Setup customer
        Customer customer = new Customer();
        customer.useStation(stsAR[0]);

        // Add items to cart
        customer.shoppingCart.add(item1);
        customer.shoppingCart.add(item2);
        customer.shoppingCart.add(item3);

        // Create cards
        Card creditCard = new Card("Credit", "0000111122223333", "John Doe", "012", "345", true, true);
        Card debitCard = new Card("Debit", "1111111122223333", "Dave", "321", "555", true, true);

        // Add card to customer waller
        customer.wallet.cards.add(creditCard);
        customer.wallet.cards.add(debitCard);

        // Populate card issuer
        CardIssuer cardIssuer = new CardIssuer("Credit", 10);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        cardIssuer.addCardData(creditCard.number, creditCard.cardholder, calendar, creditCard.cvv, 10000);
        cardIssuer.addCardData(debitCard.number, debitCard.cardholder, calendar, debitCard.cvv, 15000);

        // Setup station logic
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);

        // Work around for veryfing card payments
        HashMap<Card, CardIssuer> bankingInfo = new HashMap<>();
        bankingInfo.put(creditCard, cardIssuer);
        bankingInfo.put(debitCard, cardIssuer);
        /*
        // Setup station logic - 
        DoItYourselfStationLogic stationLogic = new DoItYourselfStationLogic(station);

        */

        // Changes above to accept multiple stations as list based on size of store
        DoItYourselfStationLogic[] stsLG = new DoItYourselfStationLogic[locationSize];
        for (int i = 0; i < locationSize; i++) {
            DoItYourselfStationLogic sLogic = new DoItYourselfStationLogic(stsAR[i]);
            stsLG[i] = sLogic;
        }

        // Whoops...
        //System.out.print(AttendantStationLogic.getInstance());
        AttendantStationLogic attendantLogic = AttendantStationLogic.getInstance();
        attendantLogic.quantizeStations(stsLG);
        attendantLogic.bind();    // note that DIY stations are (SHOULD BE) all bound
        //TEST ON BELOW LINE - works nicely :)
        //System.out.print("\n" + attendantLogic.matchStationID(stsLG[1]) + "\n");


        // Start at welcome screen
        // Initialize GUI here...
        TouchScreen screen = new TouchScreen();
        screen.plugIn();
        screen.turnOn();

        // Ideally we should be able to create multiple customer GUI's, but for the purposes of this demo, one is enough
        stsLG[0].station.plugIn();
        stsLG[0].station.turnOn();
        //CustomerGUI customerGUI = new CustomerGUI(stsLG[0], customer, screen, bankingInfo);
        stsLG[0].initCustomerGUI(stsLG[0], customer, screen, bankingInfo, bagBarcode);
      
        // TEST
        //stsLG[0].bagApproval();
        //System.out.print("\nFrom Demo: " + stsLG[0].getStation() + "\n");

        // BELOW IS FOR HANDLING w/o GUI
        //attendantLogic.attendantDecision(0, true);
        //System.out.print("\nDone...\n");

    }
}
