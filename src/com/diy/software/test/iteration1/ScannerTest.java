package com.diy.software.test.iteration1;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import org.junit.Before;
import org.junit.Test;

import static com.diy.software.DoItYourselfStationLogic.Status.READY;
import static org.junit.Assert.*;

public class ScannerTest {
    DoItYourselfStationAR station;
    DoItYourselfStationLogic stationLogic;
    Customer customer;

    Barcode barcode = new Barcode(new Numeral[]{Numeral.valueOf((byte) 1)});
    BarcodedItem item1 = new BarcodedItem(barcode, 5);
    BarcodedItem item2 = new BarcodedItem(barcode, 5);
    BarcodedItem item3 = new BarcodedItem(barcode, 5);
    BarcodedProduct product = new BarcodedProduct(barcode, "Test product", 10, 5);

    @Before
    public void setUp() {
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
        station = new DoItYourselfStationAR();
        stationLogic = new DoItYourselfStationLogic(station);
        customer = new Customer();
        customer.useStation(station);
        customer.shoppingCart.add(item1);
        customer.shoppingCart.add(item2);
        customer.shoppingCart.add(item3);
        customer.selectNextItem();
    }

    @Test
    public void ScanItemsTest() {
        assertTrue(stationLogic.scannerController.getScannedItems().isEmpty());
        scanHelper();
        assertEquals(1, stationLogic.scannerController.getScannedItems().size());
        scanHelper();
        scanHelper();
        assertEquals(3, stationLogic.scannerController.getScannedItems().size());
    }

    @Test
    public void TrackTotalTest() {
        assertEquals(0.0, stationLogic.scannerController.getTotal(), 0.0);
        scanHelper();
        assertEquals(10.0, stationLogic.scannerController.getTotal(), 0.0);
        scanHelper();
        scanHelper();
        assertEquals(30.0, stationLogic.scannerController.getTotal(), 0.0);
    }

    @Test
    public void ResetTest() {
        assertTrue(stationLogic.scannerController.getScannedItems().isEmpty());
        assertEquals(0.0, stationLogic.scannerController.getTotal(), 0.0);
        scanHelper();
        scanHelper();
        assertFalse(stationLogic.scannerController.getScannedItems().isEmpty());
        assertEquals(20.0, stationLogic.scannerController.getTotal(), 0.0);
        stationLogic.scannerController.reset();
        assertTrue(stationLogic.scannerController.getScannedItems().isEmpty());
        assertEquals(0, stationLogic.scannerController.getTotal(), 0.0);
    }

    /**
     * Helper function to scan an item and select the next item afterwards
     * Re-scans the item if the scanning failed
     */
    private void scanHelper() {
        while (stationLogic.getStatus() == READY)
            customer.scanItem();
        customer.placeItemInBaggingArea();
        if (customer.shoppingCart.size() >= 1)
            customer.selectNextItem();
    }
}