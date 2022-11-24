package com.diy.software.test.iteration2;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.DoItYourselfStationAR;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.software.AttendantStationLogic;
import com.diy.software.DoItYourselfStationLogic;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import org.junit.Before;
import org.junit.Test;

import static com.diy.software.DoItYourselfStationLogic.Status.*;
import static org.junit.Assert.assertEquals;

public class ScaleTest {
    DoItYourselfStationAR station;
    DoItYourselfStationLogic stationLogic;
    Customer customer;

    Barcode barcode1 = new Barcode(new Numeral[]{Numeral.valueOf((byte) 1)});
    BarcodedItem item1 = new BarcodedItem(barcode1, 5);
    Barcode barcode2 = new Barcode(new Numeral[]{Numeral.valueOf((byte) 2)});
    BarcodedItem item2 = new BarcodedItem(barcode2, 5);
    BarcodedProduct product1 = new BarcodedProduct(barcode1, "Test product 1", 10, 5);
    BarcodedProduct product2 = new BarcodedProduct(barcode1, "Test product 2", 10, 10);

    @Before
    public void setUp() {
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
        station = new DoItYourselfStationAR();
        stationLogic = new DoItYourselfStationLogic(station);
        AttendantStationLogic.getInstance().quantizeStations(new DoItYourselfStationLogic[]{stationLogic});
        customer = new Customer();
        customer.useStation(station);
        customer.shoppingCart.add(item2);
        customer.shoppingCart.add(item1);
        customer.selectNextItem();
    }

    @Test
    public void WeighItemsTest() {
        assertEquals(0.0, stationLogic.scaleController.getExpectedWeightInGrams(), 0.0);
        assertEquals(READY, stationLogic.getStatus());
        scanHelper();
        assertEquals(WAITING_FOR_WEIGHT, stationLogic.getStatus());
        customer.placeItemInBaggingArea();
        assertEquals(5.0, stationLogic.scaleController.getExpectedWeightInGrams(), 0.0);
        assertEquals(READY, stationLogic.getStatus());
    }

    @Test
    public void DiscrepancyTest() {
        assertEquals(0.0, stationLogic.scaleController.getExpectedWeightInGrams(), 0.0);
        assertEquals(READY, stationLogic.getStatus());
        scanHelper();
        assertEquals(WAITING_FOR_WEIGHT, stationLogic.getStatus());
        customer.placeItemInBaggingArea();
        assertEquals(5.0, stationLogic.scaleController.getExpectedWeightInGrams(), 0.0);
        assertEquals(READY, stationLogic.getStatus());
        customer.selectNextItem();
        scanHelper();
        assertEquals(WAITING_FOR_WEIGHT, stationLogic.getStatus());
        customer.placeItemInBaggingArea();
        assertEquals(15.0, stationLogic.scaleController.getExpectedWeightInGrams(), 0.0);
        assertEquals(DISCREPANCY, stationLogic.getStatus());
    }

    @Test
    public void ResetTest() {
        assertEquals(0.0, stationLogic.scaleController.getExpectedWeightInGrams(), 0.0);
        assertEquals(READY, stationLogic.getStatus());
        scanHelper();
        stationLogic.scaleController.reset();
        assertEquals(0.0, stationLogic.scaleController.getExpectedWeightInGrams(), 0.0);
        assertEquals(READY, stationLogic.getStatus());
    }

    /**
     * Helper function to scan an item
     * Re-scans the item if the scanning failed
     */
    private void scanHelper() {
        while (stationLogic.getStatus() == READY)
            customer.scanItem();
    }
}