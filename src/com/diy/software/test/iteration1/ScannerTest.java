package com.diy.software.test.iteration1;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScannerTest {
    DoItYourselfStationAR station;
    DoItYourselfStationLogic stationLogic;
    Customer customer;

    Barcode barcode = new Barcode(new Numeral[]{Numeral.valueOf((byte) 1)});
    BarcodedItem item = new BarcodedItem(barcode, 10);
    BarcodedProduct product = new BarcodedProduct(barcode, "Test product", 10, 5);

    @Before
    public void setUp() {
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
        station = new DoItYourselfStationAR();
        stationLogic = new DoItYourselfStationLogic(station);
        customer = new Customer();
        customer.useStation(station);
        customer.shoppingCart.add(item);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void ScanItemsTest() {
        assertTrue(stationLogic.scannerController.getScannedItems().isEmpty());
        customer.selectNextItem();
        customer.scanItem();
        assertEquals(1, stationLogic.scannerController.getScannedItems().size());
        customer.scanItem();
        customer.scanItem();
        assertEquals(3, stationLogic.scannerController.getScannedItems().size());
    }

    @Test
    public void TrackTotalTest() {
        assertEquals(0.0, stationLogic.scannerController.getTotal(), 0.0);
        customer.selectNextItem();
        customer.scanItem();
        assertEquals(10.0, stationLogic.scannerController.getTotal(), 0.0);
        customer.scanItem();
        customer.scanItem();
        assertEquals(30.0, stationLogic.scannerController.getTotal(), 0.0);
    }

    @Test
    public void ResetTest() {
        assertTrue(stationLogic.scannerController.getScannedItems().isEmpty());
        assertEquals(0.0, stationLogic.scannerController.getTotal(), 0.0);
        customer.selectNextItem();
        customer.scanItem();
        customer.scanItem();
        assertFalse(stationLogic.scannerController.getScannedItems().isEmpty());
        assertEquals(20.0, stationLogic.scannerController.getTotal(), 0.0);
        stationLogic.scannerController.reset();
        assertTrue(stationLogic.scannerController.getScannedItems().isEmpty());
        assertEquals(0, stationLogic.scannerController.getTotal(), 0.0);
    }


//    @Test
//    public void ItemWeightTest() {
//        assertTrue(test.getScannedItems().isEmpty());
//        test.Scan(item);
//        assertEquals(5.0, test.getExpectedWeight(), 0);
//    }
//
//    @Test
//    public void ItemCostTest() {
//        assertTrue(test.getScannedItems().isEmpty());
//        test.Scan(item);
//        assertEquals(2.0, test.getTotal(), 0);
//    }
//
//    @Test
//    public void failedScan() {
//        while (true) {
//            try {
//                test.Scan(item);
//            } catch (ScanFailureException e) {
//                return;
//            }
//        }
//    }
}