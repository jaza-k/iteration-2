package com.diy.software.test.iteration2;

import ca.powerutility.PowerSurge;
import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.CardIssuer;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.diy.software.payment.AddItemAfterPartialPayment;
import com.diy.software.payment.Payment;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.opeechee.Card;
import com.unitedbankingservices.DisabledException;
import com.unitedbankingservices.TooMuchCashException;
import com.unitedbankingservices.banknote.Banknote;
import com.unitedbankingservices.coin.Coin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;

public class AddItemAfterPartialPaymentTest {

    Barcode barcode1;
    Barcode barcode2;
    Barcode barcode3;
    BarcodedItem item1;
    BarcodedItem item2;
    BarcodedItem item3;
    BarcodedProduct product1;
    BarcodedProduct product2;
    BarcodedProduct product3;
    Customer customer;
    Banknote twentybill, tenbill, fivebill, fiftybill, hundredbill;
    Coin penny, nickel, dime, quarter, loonie, toonie; //For the sake of simplicity, we'll assume this test takes place in an alternate universe where canadian pennies are still in circulation
    boolean flag;
    int[] denominations;
    long[] coindenoms;
    static DoItYourselfStationLogic stationLogic;
    DoItYourselfStationAR station;
    Card card;
    CardIssuer creditIssuer;

    Date date;
    Calendar c;

    @Before
    public void setup() {
        denominations = new int[]{5, 10, 20, 50, 100};
        coindenoms = new long[]{1, 5, 10, 25, 100, 200};
        DoItYourselfStationAR.configureBanknoteDenominations(denominations); //Note that the static call to configure banknote denominations
        //is done BEFORE constructing a station
        DoItYourselfStationAR.configureBanknoteStorageUnitCapacity(4); //Only four bills can fit in station2's storage unit
        DoItYourselfStationAR.configureCoinDenominations(coindenoms);
        station = new DoItYourselfStationAR();
        station.plugIn();
        station.turnOn();

        // Create barcodes
        barcode1 = new Barcode(new Numeral[]{Numeral.one});
        barcode2 = new Barcode(new Numeral[]{Numeral.two, Numeral.three});
        barcode3 = new Barcode(new Numeral[]{Numeral.one, Numeral.two, Numeral.three});

        // Create barcoded items
        item1 = new BarcodedItem(barcode1, 15);
        item2 = new BarcodedItem(barcode2, 25);
        item3 = new BarcodedItem(barcode3, 35);

        // Create barcoded products
        product1 = new BarcodedProduct(barcode1, "Creamed Eel", 5, 15);
        product2 = new BarcodedProduct(barcode2, "Gum & Nuts", 10, 25);
        product3 = new BarcodedProduct(barcode3, "Tripe", 20, 35);

        // Add barcoded products to database
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode3, product3);

        // Setup customer
        customer = new Customer();
        customer.useStation(station);

        // Create cards
        card = new Card("Credit", "0000111122223333", "John Doe", "012", "345", true, true);

        // Add card to customer waller
        customer.wallet.cards.add(card);

        // Populate card issuer
        creditIssuer = new CardIssuer("Credit", 10);
        date = new Date();
        c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        creditIssuer.addCardData(card.number, card.cardholder, c, card.cvv, 10000);

        tenbill = new Banknote(Currency.getInstance("CAD"), 10);

        // Setup station logic
        stationLogic = new DoItYourselfStationLogic(station);
    }

    @After
    public void teardown() {
        ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
        customer.wallet.cards.clear();
        stationLogic.scannerController.reset();
    }

    @Test
    public void PartialCredit() {
        boolean flag;
        stationLogic.scannerController.barcodeScanned(station.scanner, item3.getBarcode());
        assertEquals(20.0, stationLogic.scannerController.getTotal());
        Payment newpay = new Payment(station, stationLogic.scannerController.getTotal());
        assertEquals(20.0, newpay.checkoutTotal);
        try {
            while (!newpay.CreditPay("345", card, 10, creditIssuer));
        } catch (PowerSurge e) { }
        assertEquals(10.0, newpay.checkoutTotal); //After paying ten dollars, there should still be ten dollars left to pay
        assertEquals(20.0, stationLogic.scannerController.getTotal()); //The checkout total for the scanner controller should still be 20
        AddItemAfterPartialPayment.AddAfterPartial(newpay, stationLogic.scannerController); //This will allow us to add more items after partially paying
        assertEquals(10.0, stationLogic.scannerController.getTotal());
    }

    @Test
    public void PartialCash() throws TooMuchCashException, DisabledException {
        stationLogic.scannerController.barcodeScanned(station.scanner, item3.getBarcode());
        assertEquals(20.0, stationLogic.scannerController.getTotal());
        Payment newpay = new Payment(station, stationLogic.scannerController.getTotal());
        station.banknoteInput.receive(tenbill);
        assertEquals(10.0, newpay.checkoutTotal);
        assertEquals(20.0, stationLogic.scannerController.getTotal());
        AddItemAfterPartialPayment.AddAfterPartial(newpay, stationLogic.scannerController);
        assertEquals(10.0, stationLogic.scannerController.getTotal());
    }
}