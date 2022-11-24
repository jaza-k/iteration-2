package com.diy.software;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.gui.CustomerGUI;
import com.jimmyselectronics.disenchantment.TouchScreen;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.opeechee.Card;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Demo {
    public static void main(String[] args) {
        // Create station - MOVED BELOW
        //DoItYourselfStationAR station = new DoItYourselfStationAR();
    	
        // Changes above to accept multiple stations as list based on size of store
        int locationSize = 6;	// 6 is arbitrary value representing a hypothetical store size (may limit to 99 later)
        DoItYourselfStationAR[] stsAR = new DoItYourselfStationAR[locationSize];
        for (int i = 0; i < locationSize; i++) {
        	DoItYourselfStationAR station = new DoItYourselfStationAR();
        	stsAR[i] = station;
        }

        // Create barcodes
        Barcode barcode1 = new Barcode(new Numeral[] { Numeral.one });
        Barcode barcode2 = new Barcode(new Numeral[] { Numeral.two, Numeral.three });
        Barcode barcode3 = new Barcode(new Numeral[] { Numeral.one, Numeral.two, Numeral.three });

        // Create barcoded items
        BarcodedItem item1 = new BarcodedItem(barcode1, 1);
        BarcodedItem item2 = new BarcodedItem(barcode2, 1);
        BarcodedItem item3 = new BarcodedItem(barcode3, 1);

        // Create barcoded products
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "Food", 5, 15);
        BarcodedProduct product2 = new BarcodedProduct(barcode2, "More Food", 10, 25);
        BarcodedProduct product3 = new BarcodedProduct(barcode3, "All The Food", 20, 35);

        // Add barcoded products to database
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode3, product3);

      
        // Setup customer
        Customer customer = new Customer();
        customer.useStation(stsAR[0]);

        // Add items to cart
        customer.shoppingCart.add(item1);
        customer.shoppingCart.add(item2);
        customer.shoppingCart.add(item3);

        // Create cards
        Card card = new Card("Credit", "0000111122223333", "John Doe", "012", "345", true, true);
      
        

        // Add card to customer waller
        customer.wallet.cards.add(card);

        // Populate card issuer
        CardIssuer creditIssuer = new CardIssuer("Credit", 10);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        creditIssuer.addCardData(card.number, card.cardholder, c, card.cvv, 10000);
        // Work around for veryfing card payments
        HashMap<Card, CardIssuer> bankingInfo = new HashMap<>();
        bankingInfo.put(card, creditIssuer);
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
        attendantLogic.bind();	// note that DIY stations are (SHOULD BE) all bound
        //TEST ON BELOW LINE - works nicely :)
        //System.out.print("\n" + attendantLogic.matchStationID(stsLG[1]) + "\n");
        
        
        // Start at welcome screen
        // Initialize GUI here...
        TouchScreen screen = new TouchScreen();
        screen.plugIn();
        screen.turnOn();
        
        // Ideally we should be able to create multiple customer GUI's, but for the purposes of this demo, one is enough
        CustomerGUI customerGUI = new CustomerGUI(stsLG[0], customer, screen, bankingInfo);
        
        
        // TEST
        //stsLG[0].bagApproval();
        //System.out.print("\nFrom Demo: " + stsLG[0].getStation() + "\n");
        
        // BELOW IS FOR HANDLING w/o GUI
        //attendantLogic.attendantDecision(0, true);
        //System.out.print("\nDone...\n");
	        
    }
}