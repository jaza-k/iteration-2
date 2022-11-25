package com.diy.software.test.iteration2;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.AttendantStationLogic;
import com.diy.software.DoItYourselfStationLogic;
import com.diy.software.controllers.ScannerController;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddItemByScanningTest {

    DoItYourselfStationAR station;

    DoItYourselfStationLogic stationLogic;
    Customer customer;

    //create dummy items and products
    Barcode barcode1 = new Barcode(new Numeral[]{Numeral.valueOf((byte) 1)});
    BarcodedItem item1 = new BarcodedItem(barcode1, 5);
    Barcode barcode2 = new Barcode(new Numeral[]{Numeral.valueOf((byte) 2)});
    BarcodedItem item2 = new BarcodedItem(barcode2, 5);

    // create barcoded product
    BarcodedProduct product1 = new BarcodedProduct(barcode1, "Test product 1", 10, 5);
    BarcodedProduct product2 = new BarcodedProduct(barcode2, "Test product 2", 10, 10);

    //Set before tests run
    @Before
    public void setup() {
        ScannerController scanControl = new ScannerController(stationLogic);

        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
        station = new DoItYourselfStationAR();
        stationLogic = new DoItYourselfStationLogic(station);
        AttendantStationLogic.getInstance().quantizeStations(new DoItYourselfStationLogic[]{stationLogic});
        customer = new Customer();
        customer.useStation(station);

        station.plugIn();
        station.turnOn();


    }

    //Testing when scanning a valid item
    @Test
    public void validItemScanned() {

        customer.shoppingCart.add(item1);
        customer.selectNextItem();
        customer.scanItem();
        customer.placeItemInBaggingArea();
        assertEquals(1, stationLogic.scannerController.getScannedItems().size());

    }

    //testing when not scanning from an empty db and shopping cart
    @Test
    public void noItemScanned() {
        ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(barcode1, product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(barcode2, product2);

        station.plugIn();
        station.turnOn();
        customer.shoppingCart.add(item1);
        customer.selectNextItem();
        customer.scanItem();

        assertTrue(stationLogic.scannerController.getScannedItems().isEmpty());
    }

    //testing scanning when disabled
    @Test
    public void scannerDisabled() {

        station.plugIn();
        station.turnOn();

        station.scanner.disable();

        customer.shoppingCart.add(item1);
        customer.selectNextItem();
        customer.scanItem();

        assertTrue(stationLogic.scannerController.getScannedItems().isEmpty());

    }

    //Testing when scanning two items
    @Test
    public void scanTwoItems() {
        station.scanner.enable();
        customer.shoppingCart.add(item1);
        customer.selectNextItem();
        customer.scanItem();
        customer.placeItemInBaggingArea();
        customer.shoppingCart.add(item2);

        customer.selectNextItem();
        customer.scanItem();
        // System.out.println(stationLogic.scannerController.getScannedItems().size());

        assertEquals(2, stationLogic.scannerController.getScannedItems().size());

    }

}